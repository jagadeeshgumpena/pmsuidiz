package com.dizitiveit.pms.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.Dao.BuildingSecurityDao;
import com.dizitiveit.pms.Dao.ReLoginDetailsDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.Dao.SecurityLoginDetailsDao;
import com.dizitiveit.pms.Dao.SecurityShiftsDao;
import com.dizitiveit.pms.model.BuildingSecurity;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.ReLoginDetails;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.SecurityLoginDetails;
import com.dizitiveit.pms.model.SecurityShifts;
import com.dizitiveit.pms.model.Transactions;
import com.dizitiveit.pms.pojo.BuildingSecurityPojo;
import com.dizitiveit.pms.pojo.ReLoginDetailsPojo;
import com.dizitiveit.pms.pojo.SecurityLoginDetailsPojo;
import com.dizitiveit.pms.pojo.SecurityShiftsPojo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.mail.iap.Response;

@RestController
@RequestMapping("/shifts")
public class SecurityShiftsController {
	
	@Autowired
	private SecurityShiftsDao securityShiftsDao;
	
	@Autowired
	private BuildingSecurityDao buildingSecurityDao;
	
	@Autowired
	private ResponsesDao responsesDao;
	
	@Autowired
	private SecurityLoginDetailsDao securityLoginDetailsDao;
	
	@Autowired
	private ReLoginDetailsDao reLoginDetailsDao;

	@PostMapping("/saveShifts/{securityId}")
	public ResponseEntity<?> saveShifts(@PathVariable long securityId,@RequestBody SecurityShifts securityShifts){
		BuildingSecurity buildingSecurity = buildingSecurityDao.findBySecurityId(securityId);
		
		Date date= new Date();
		Instant inst = date.toInstant();
		 LocalDate localDate = inst.atZone(ZoneId.systemDefault()).toLocalDate();
		  Instant dayInst = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
		   Date day = Date.from(dayInst);
		   System.out.println(day);
		   Date dateTomorrow=DateUtils.addDays(day, 1);
		   dateTomorrow=DateUtils.addMinutes(dateTomorrow, -1);
		   System.out.println(dateTomorrow);
		   
		List<SecurityShifts> securityShiftsexist=securityShiftsDao.findByShiftSlotBySecurityId(securityId, true);
		List<SecurityShifts> securityShiftsnew= securityShiftsDao.findAllByendDateBetween(day,dateTomorrow);
        Predicate<SecurityShifts> condition = x -> x.getBuildingSecurity().getSecurityId()!=securityId;

		securityShiftsnew.removeIf(condition);
		System.out.println(condition);
		/*
		 * for(int i=0;i<securityShiftsnew.size();i++) {
		 * if(securityShiftsnew.get(i).getBuildingSecurity().getSecurityId()!=
		 * securityId) { securityShiftsnew.remove(i);
		 * System.out.println(securityShiftsnew.get(i).getBuildingSecurity());
		 * 
		 * } }
		 */
		System.out.println(securityShiftsnew.size());
		System.out.println(securityShiftsexist.size());
		if(securityShiftsexist.size()==(securityShiftsnew.size()))
		{
			if(securityShiftsnew.size()>0)
			{
				Instant instStartDate = securityShifts.getStartDate().toInstant();
				 LocalDate localDateStart = instStartDate.atZone(ZoneId.systemDefault()).toLocalDate();
				  Instant dayInstStart = localDateStart.atStartOfDay(ZoneId.systemDefault()).toInstant();
				   Date dayStart = Date.from(dayInstStart);
				System.out.println(dayStart);
				System.out.println(day);
				if(dayStart.equals(day))
				{
					 Responses responses = responsesDao.findById(45);
						return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
					
				}
				
			}
			System.out.println("in if");
		securityShifts.setBuildingSecurity(buildingSecurity);
		/*
		 * Date date = securityShifts.getStartDate(); Instant inst = date.toInstant();
		 * LocalDate localDate = inst.atZone(ZoneId.systemDefault()).toLocalDate();
		 * Instant dayInst = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
		 * Date day = Date.from(dayInst);
		 */
       System.out.println(day);
		if(securityShifts.getShiftSlot().equalsIgnoreCase("Morning")) {
			
			Date startDate = DateUtils.addHours(day,+6);
			Date endDate =  DateUtils.addHours(startDate,+8);
			  LocalTime start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

			//LocalTime start = LocalTime.ofInstant(startDate.toInstant(),ZoneId.systemDefault());
			//LocalTime end = LocalTime.ofInstant(endDate.toInstant(),ZoneId.systemDefault());
			LocalTime end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
			securityShifts.setStartTime(start);
			securityShifts.setEndTime(end);
			securityShifts.setActiveSlot(true);
		}
		else if(securityShifts.getShiftSlot().equalsIgnoreCase("Afternoon"))
		{
			Date startDate = DateUtils.addHours(day,+14);
			Date endDate =  DateUtils.addHours(startDate,+8);
			
			 LocalTime start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
			 LocalTime end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
			securityShifts.setStartTime(start);
			securityShifts.setEndTime(end);
			securityShifts.setActiveSlot(true);
		}
		else if(securityShifts.getShiftSlot().equalsIgnoreCase("Night"))
		{
			Date startDate = DateUtils.addHours(day,+22);
			Date endDate =  DateUtils.addHours(startDate,+8);
			
			 LocalTime start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
			 LocalTime end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
			securityShifts.setStartTime(start);
			securityShifts.setEndTime(end);
			securityShifts.setActiveSlot(true);
			
		}
		System.out.println(securityShifts.toString());
		securityShifts.setStartDate(securityShifts.getStartDate());
		securityShifts.setEndDate(securityShifts.getEndDate());
		securityShiftsDao.save(securityShifts);
		 Responses responses = responsesDao.findById(38);
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		}
		else {
			 Responses responses = responsesDao.findById(40);
				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		}
	}
	
	@PostMapping("/updateShifts/{shiftId}")
	public ResponseEntity<?> updateShifts(@PathVariable long shiftId,@RequestBody SecurityShifts securityShifts){
		SecurityShifts securityShiftsUpdate = securityShiftsDao.findByShiftId(shiftId);	
		Date date = securityShifts.getStartDate();
		Instant inst = date.toInstant();
		 LocalDate localDate = inst.atZone(ZoneId.systemDefault()).toLocalDate();
		  Instant dayInst = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
		   Date day = Date.from(dayInst);
       System.out.println(day);
		if(securityShifts.getShiftSlot().equalsIgnoreCase("Morning")) {
			System.out.println("in morning");
			Date startDate = DateUtils.addHours(day,+6);
			Date endDate =  DateUtils.addHours(startDate,+8);
			  LocalTime start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

			//LocalTime start = LocalTime.ofInstant(startDate.toInstant(),ZoneId.systemDefault());
			//LocalTime end = LocalTime.ofInstant(endDate.toInstant(),ZoneId.systemDefault());
			LocalTime end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
			securityShiftsUpdate.setShiftSlot(securityShifts.getShiftSlot());
			securityShiftsUpdate.setStartTime(start);
			securityShiftsUpdate.setEndTime(end);
			securityShiftsUpdate.setActiveSlot(true);
		}
		else if(securityShifts.getShiftSlot().equalsIgnoreCase("Afternoon"))
		{
			Date startDate = DateUtils.addHours(day,+14);
			Date endDate =  DateUtils.addHours(startDate,+8);
			/*
			 * securityShiftsUpdate.setStartDate(startDate);
			 * securityShiftsUpdate.setEndDate(endDate);
			 */
			 LocalTime start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
			 LocalTime end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
			 securityShiftsUpdate.setShiftSlot(securityShifts.getShiftSlot());
			 securityShiftsUpdate.setStartTime(start);
			 securityShiftsUpdate.setEndTime(end);
			 securityShiftsUpdate.setActiveSlot(true);
		}
		else if(securityShifts.getShiftSlot().equalsIgnoreCase("Night"))
		{
			Date startDate = DateUtils.addHours(day,+22);
			Date endDate =  DateUtils.addHours(startDate,+8);
			/*
			 * securityShiftsUpdate.setStartDate(startDate);
			 * securityShiftsUpdate.setEndDate(endDate);
			 */
			 LocalTime start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
			 LocalTime end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
			 securityShiftsUpdate.setShiftSlot(securityShifts.getShiftSlot());
			securityShiftsUpdate.setStartTime(start);
			securityShiftsUpdate.setEndTime(end);
			securityShifts.setActiveSlot(true);
			
		}
		System.out.println(securityShiftsUpdate.toString());
		securityShiftsUpdate.setStartDate(securityShifts.getStartDate());
		securityShiftsUpdate.setEndDate(securityShifts.getEndDate());
		securityShiftsDao.save(securityShiftsUpdate);
		 Responses responses = responsesDao.findById(38);
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
	}
	
	@GetMapping("/getShiftsBySlot/{securityId}/{shiftSlot}")
	public ResponseEntity<?> getShiftsBySlot(@PathVariable long securityId,@PathVariable String shiftSlot){
		List<SecurityShifts> listSecurityShifts = securityShiftsDao.findByShiftSlot(shiftSlot,true);
		 HashMap<String, List<SecurityShifts>> response = new HashMap<String,List<SecurityShifts>>();
         response.put("listSecurityShifts",listSecurityShifts);
	 return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/getShitsByDate/{startDate}/{endDate}")
	public ResponseEntity<?> getShitsByDate(@PathVariable  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,@PathVariable  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate){
		List<SecurityShifts> listsecurityShifts = securityShiftsDao.findByShiftSlotWithDate( startDate, endDate);
			 HashMap<String, List<SecurityShifts>> response = new HashMap<String,List<SecurityShifts>>();
	         response.put("listsecurityShifts",listsecurityShifts);
		 return ResponseEntity.ok(response);
		
	}
	
	@GetMapping("getshifts/{type}/{value}")
	public ResponseEntity<?> getshifts(@PathVariable String type,@PathVariable String value){
		
		//List<BuildingSecurityPojo> list= new ArrayList();
		
		if(type.equalsIgnoreCase("SecurityId"))
		{
			List<BuildingSecurityPojo> list= new ArrayList();
			BuildingSecurityPojo buildingSecurityPojo = new BuildingSecurityPojo();
	
			int securityId=Integer.parseInt(value);  
			
			BuildingSecurity buildingSecurity = buildingSecurityDao.findBySecurityId(securityId);
			buildingSecurityPojo.setBuildingSecurity(buildingSecurity);
			/*
			 * buildingSecurityPojo.setSecurityId(buildingSecurity.getSecurityId());
			 * buildingSecurityPojo.setFirstName(buildingSecurity.getFirstName());
			 * buildingSecurityPojo.setLastName(buildingSecurity.getLastName());
			 * buildingSecurityPojo.setEmail(buildingSecurity.getEmail());
			 * buildingSecurityPojo.setMobile(buildingSecurity.getMobile());
			 * buildingSecurityPojo.setAddress(buildingSecurity.getAddress());
			 * buildingSecurityPojo.setNationalCard(buildingSecurity.getNationalCard());
			 */
			List<SecurityShifts> securityShifts=securityShiftsDao.findByBuildingSecurity(buildingSecurity);
			List<SecurityShiftsPojo> listsecurityShiftPojo= new ArrayList();
			
			for(SecurityShifts securityShift : securityShifts)
			{
				SecurityShiftsPojo shiftsPojo = new SecurityShiftsPojo();
				shiftsPojo.setSecurityId(securityShift.getBuildingSecurity().getSecurityId());
				shiftsPojo.setShiftSlot(securityShift.getShiftSlot());
				shiftsPojo.setStartTime(securityShift.getStartTime());
				shiftsPojo.setEndTime(securityShift.getEndTime());
				shiftsPojo.setStartDate(securityShift.getStartDate());
				shiftsPojo.setEndDate(securityShift.getEndDate());
				listsecurityShiftPojo.add(shiftsPojo);
			}
			buildingSecurityPojo.setSecurityShifts(listsecurityShiftPojo);
			List<SecurityLoginDetails> listsecurityLoginDetails = securityLoginDetailsDao.findByBuildingSecurity(buildingSecurity);
			List<SecurityLoginDetailsPojo> listsecurityLoginPojo= new ArrayList();
			for(SecurityLoginDetails securityLoginDetails : listsecurityLoginDetails)
				{
			SecurityLoginDetailsPojo securityLoginDetailsPojo = new SecurityLoginDetailsPojo();
			//securityLoginDetailsPojo.setSecurityLoginId(securityLoginDetails.getBuildingSecurity().getSecurityId());
			DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
			if(securityLoginDetails.getInTime()!=null) {
			securityLoginDetailsPojo.setInTime(df.format(securityLoginDetails.getInTime()));
			}
			if(securityLoginDetails.getOutTime()!=null) {
			securityLoginDetailsPojo.setOutTime(df.format(securityLoginDetails.getOutTime()));
			}
			securityLoginDetailsPojo.setPurpose(securityLoginDetails.getPurpose());
			listsecurityLoginPojo.add(securityLoginDetailsPojo);
				}
			buildingSecurityPojo.setSecurityLoginDetails(listsecurityLoginPojo);
				List<ReLoginDetails> listReLoginDetails = reLoginDetailsDao.findByBuildingSecurity(buildingSecurity);
				List<ReLoginDetailsPojo> listReLoginPojo= new ArrayList();
				for(ReLoginDetails reLoginDetails : listReLoginDetails)
				{
					ReLoginDetailsPojo reLoginDetailsPojo= new ReLoginDetailsPojo();
					DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
					if(reLoginDetails.getReLoginTime()!=null) {
					reLoginDetailsPojo.setReLoginTime(df.format(reLoginDetails.getReLoginTime()));
					}
					listReLoginPojo.add(reLoginDetailsPojo);
				}
				
				buildingSecurityPojo.setReLoginDetails(listReLoginPojo);
			
				list.add(buildingSecurityPojo);
				 HashMap<String,List<BuildingSecurityPojo>> response = new HashMap<String,List<BuildingSecurityPojo>>();
					response.put("buildingSecurityPojo", list);
					
					
					/*
					 * ObjectMapper mapper = new ObjectMapper(); DateFormat df = new
					 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); mapper.setDateFormat(df);
					 * 
					 * 
					 * String responseMap=""; try { responseMap =
					 * mapper.writeValueAsString(securityLogin); } catch (JsonProcessingException e)
					 * { // TODO Auto-generated catch block e.printStackTrace(); }
					 */
					System.out.println(response);
					return ResponseEntity.ok(response);
			}
		else if(type.equalsIgnoreCase("Shift Slot"))
		{
			System.out.println("in slot");
			List<BuildingSecurityPojo> buildingSecurityPojoList= new ArrayList();
			List<SecurityShifts> securityShifts=securityShiftsDao.findByShiftSlot(value,true);
			System.out.println(securityShifts.size());
			
			List<BuildingSecurity> buildingSecurityList= new ArrayList();
			
			for(SecurityShifts securityShift : securityShifts)
			{
				if(!buildingSecurityList.contains(securityShift.getBuildingSecurity()))
				{
					buildingSecurityList.add(securityShift.getBuildingSecurity());
					
				}
			}
			System.out.println(buildingSecurityList.size());
			
			for(BuildingSecurity buildingSecurity : buildingSecurityList)
			{
				
				BuildingSecurityPojo  buildingSecurityPojo = new BuildingSecurityPojo(); 
				buildingSecurityPojo.setBuildingSecurity(buildingSecurity);
				/*
				 * buildingSecurityPojo.setSecurityId(buildingSecurity.getSecurityId());
				 * buildingSecurityPojo.setFirstName(buildingSecurity.getFirstName());
				 * buildingSecurityPojo.setLastName(buildingSecurity.getLastName());
				 * buildingSecurityPojo.setEmail(buildingSecurity.getEmail());
				 * buildingSecurityPojo.setMobile(buildingSecurity.getMobile());
				 * buildingSecurityPojo.setAddress(buildingSecurity.getAddress());
				 * buildingSecurityPojo.setNationalCard(buildingSecurity.getNationalCard());
				 */
		
				List<SecurityShifts> securityShiftsSecurity= securityShiftsDao.findByShiftSlotBySecurityId(buildingSecurity.getSecurityId(), true);
				
				List<SecurityShiftsPojo> listsecurityShiftPojo= new ArrayList();
				for(SecurityShifts securityShift : securityShiftsSecurity)
				{
				SecurityShiftsPojo shiftsPojo = new SecurityShiftsPojo();
				shiftsPojo.setSecurityId(securityShift.getBuildingSecurity().getSecurityId());
				shiftsPojo.setShiftSlot(securityShift.getShiftSlot());
				shiftsPojo.setStartTime(securityShift.getStartTime());
				shiftsPojo.setEndTime(securityShift.getEndTime());
				shiftsPojo.setStartDate(securityShift.getStartDate());
				shiftsPojo.setEndDate(securityShift.getEndDate());
				listsecurityShiftPojo.add(shiftsPojo);
				}
				 buildingSecurityPojo.setSecurityShifts(listsecurityShiftPojo);
			
				List<SecurityLoginDetails> listsecurityLoginDetails = securityLoginDetailsDao.findByBuildingSecurity(buildingSecurity);
				List<SecurityLoginDetailsPojo> listsecurityLoginPojo= new ArrayList();
				for(SecurityLoginDetails securityLoginDetails : listsecurityLoginDetails)
				{
			SecurityLoginDetailsPojo securityLoginDetailsPojo = new SecurityLoginDetailsPojo();
			//securityLoginDetailsPojo.setSecurityLoginId(securityLoginDetails.getBuildingSecurity().getSecurityId());
			DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
			Date dateobj = securityLoginDetails.getInTime();
			System.out.println(df.format(dateobj));
			if(securityLoginDetails.getInTime()!=null) {
			securityLoginDetailsPojo.setInTime(df.format(dateobj));
			}
			if(securityLoginDetails.getOutTime()!=null) {
			securityLoginDetailsPojo.setOutTime(df.format(securityLoginDetails.getOutTime()));
			}
			securityLoginDetailsPojo.setPurpose(securityLoginDetails.getPurpose());
			listsecurityLoginPojo.add(securityLoginDetailsPojo);
				}
				 buildingSecurityPojo.setSecurityLoginDetails(listsecurityLoginPojo);
				
				List<ReLoginDetails> listReLoginDetails = reLoginDetailsDao.findByBuildingSecurity(buildingSecurity);
				List<ReLoginDetailsPojo> listReLoginPojo= new ArrayList();
				for(ReLoginDetails reLoginDetails : listReLoginDetails)
				{
					DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
					ReLoginDetailsPojo reLoginDetailsPojo= new ReLoginDetailsPojo();
					if(reLoginDetails.getReLoginTime()!=null) {
					reLoginDetailsPojo.setReLoginTime(df.format(reLoginDetails.getReLoginTime()));
					}
					listReLoginPojo.add(reLoginDetailsPojo);
				}
				
				 buildingSecurityPojo.setReLoginDetails(listReLoginPojo);
				buildingSecurityPojoList.add( buildingSecurityPojo);
			}
			
			
				
				 HashMap<String,List<BuildingSecurityPojo>> response = new HashMap<String,List<BuildingSecurityPojo>>();
					response.put("buildingSecurityPojoList", buildingSecurityPojoList);
					
					
					/*
					 * ObjectMapper mapper = new ObjectMapper(); DateFormat df = new
					 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); mapper.setDateFormat(df);
					 * 
					 * 
					 * String responseMap=""; try { responseMap =
					 * mapper.writeValueAsString(securityLogin); } catch (JsonProcessingException e)
					 * { // TODO Auto-generated catch block e.printStackTrace(); }
					 */
					System.out.println(response);
					return ResponseEntity.ok(response);
			
		}
		else if(type.equalsIgnoreCase("Date"))
		{
			List<BuildingSecurityPojo> buildingSecurityPojoList= new ArrayList();
			Date date;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
				System.out.println(date);
		
			List<SecurityShifts> securityShifts=securityShiftsDao.findBySecurityToday(date,true);
			List<BuildingSecurity> buildingSecurityList= new ArrayList();
			
			for(SecurityShifts securityShift : securityShifts)
			{
				if(!buildingSecurityList.contains(securityShift.getBuildingSecurity()))
				{
					buildingSecurityList.add(securityShift.getBuildingSecurity());
					
				}
			}
			System.out.println(securityShifts.size());
			System.out.println(buildingSecurityList.size());
			
			for(BuildingSecurity buildingSecurity : buildingSecurityList)
			{
				BuildingSecurityPojo  buildingSecurityPojo = new BuildingSecurityPojo(); 
				buildingSecurityPojo.setBuildingSecurity(buildingSecurity);
				/*
				 * buildingSecurityPojo.setSecurityId(buildingSecurity.getSecurityId());
				 * buildingSecurityPojo.setFirstName(buildingSecurity.getFirstName());
				 * buildingSecurityPojo.setLastName(buildingSecurity.getLastName());
				 * buildingSecurityPojo.setEmail(buildingSecurity.getEmail());
				 * buildingSecurityPojo.setMobile(buildingSecurity.getMobile());
				 * buildingSecurityPojo.setAddress(buildingSecurity.getAddress());
				 * buildingSecurityPojo.setNationalCard(buildingSecurity.getNationalCard());
				 */
				
				List<SecurityShifts> securityShiftsNew= new ArrayList();
				 for (SecurityShifts securityShift : securityShifts) {
				        if (securityShift.getBuildingSecurity().getSecurityId()==buildingSecurity.getSecurityId()) {
				        	securityShiftsNew.add(securityShift);
				        }
				    }
				List<SecurityShiftsPojo> listsecurityShiftPojo= new ArrayList();
				for(SecurityShifts securityShift : securityShiftsNew)
				{
				SecurityShiftsPojo shiftsPojo = new SecurityShiftsPojo();
				shiftsPojo.setSecurityId(securityShift.getBuildingSecurity().getSecurityId());
				shiftsPojo.setShiftSlot(securityShift.getShiftSlot());
				shiftsPojo.setStartTime(securityShift.getStartTime());
				shiftsPojo.setEndTime(securityShift.getEndTime());
				shiftsPojo.setStartDate(securityShift.getStartDate());
				shiftsPojo.setEndDate(securityShift.getEndDate());
				listsecurityShiftPojo.add(shiftsPojo);
				}
				buildingSecurityPojo.setSecurityShifts(listsecurityShiftPojo);
			
				List<SecurityLoginDetails> listsecurityLoginDetails = securityLoginDetailsDao.findByTodayDate(buildingSecurity.getSecurityId());
				List<SecurityLoginDetailsPojo> listsecurityLoginPojo= new ArrayList();
				for(SecurityLoginDetails securityLoginDetails : listsecurityLoginDetails)
				{
			SecurityLoginDetailsPojo securityLoginDetailsPojo = new SecurityLoginDetailsPojo();
			//securityLoginDetailsPojo.setSecurityLoginId(securityLoginDetails.getBuildingSecurity().getSecurityId());
			DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
			if(securityLoginDetails.getInTime()!=null) {
			securityLoginDetailsPojo.setInTime(df.format(securityLoginDetails.getInTime()));
			}
			if(securityLoginDetails.getOutTime()!=null) {
			securityLoginDetailsPojo.setOutTime(df.format(securityLoginDetails.getOutTime()));
			}
			securityLoginDetailsPojo.setPurpose(securityLoginDetails.getPurpose());
			listsecurityLoginPojo.add(securityLoginDetailsPojo);
				}
				buildingSecurityPojo.setSecurityLoginDetails(listsecurityLoginPojo);
				
				List<ReLoginDetails> listReLoginDetails = reLoginDetailsDao.findByTodayDate(buildingSecurity.getSecurityId());
				List<ReLoginDetailsPojo> listReLoginPojo= new ArrayList();
				for(ReLoginDetails reLoginDetails : listReLoginDetails)
				{
					ReLoginDetailsPojo reLoginDetailsPojo= new ReLoginDetailsPojo();
					DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
					if(reLoginDetails.getReLoginTime()!=null) {
					reLoginDetailsPojo.setReLoginTime(df.format(reLoginDetails.getReLoginTime()));
					}
					listReLoginPojo.add(reLoginDetailsPojo);
				}
				
				buildingSecurityPojo.setReLoginDetails(listReLoginPojo);
				buildingSecurityPojoList.add(buildingSecurityPojo);
			}
				
				 HashMap<String,List<BuildingSecurityPojo>> response = new HashMap<String,List<BuildingSecurityPojo>>();
					response.put("buildingSecurityPojoList", buildingSecurityPojoList);
			
					
					
					/*
					 * ObjectMapper mapper = new ObjectMapper(); DateFormat df = new
					 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); mapper.setDateFormat(df);
					 * 
					 * 
					 * String responseMap=""; try { responseMap =
					 * mapper.writeValueAsString(securityLogin); } catch (JsonProcessingException e)
					 * { // TODO Auto-generated catch block e.printStackTrace(); }
					 */
					System.out.println(response);
					return ResponseEntity.ok(response);
		
			}
			catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		
	}
		return null;
	}
	
	//@Scheduled(cron ="0 0 13 * * *")
	@Scheduled(cron="0 0 23 * * *")
	public void sendingMailBeforeBlockingTransactions() throws IOException {
		
		Date date = new Date();
		 System.out.println(date);
		  List<SecurityShifts>list= securityShiftsDao.findByendDateLessThan(date);
		  for(int i=0;i<list.size();i++)
			{
			  if(list.get(i).getShiftSlot().equalsIgnoreCase("Night"))
			  {
				  
			  }
			  else
			  {
			  list.get(i).setActiveSlot(false);
			  securityShiftsDao.save(list.get(i));
			  }
			}

	}
	@Scheduled(cron="0 0 6 * * *")
	public void DeactivatingSlots() throws IOException {
		
		Date date = new Date();
		 System.out.println(date);
		  List<SecurityShifts>list= securityShiftsDao.findByendDateLessThan(date);
		  for(int i=0;i<list.size();i++)
			{
			  
			  list.get(i).setActiveSlot(false);
			  securityShiftsDao.save(list.get(i));
			  
			}

	}
	
	@GetMapping("/getBeforeDate")
	public ResponseEntity<?> getBeforeDate(){
		List<SecurityShifts> list= new ArrayList();
		List<SecurityShifts> listResponse= new ArrayList();
		List<BuildingSecurity> listResponseSecurity= new ArrayList();
		
		List<BuildingSecurity> listSecurity=buildingSecurityDao.findAll();
		for(BuildingSecurity buildingSecurity : listSecurity) {
			List<SecurityShifts> securityShiftsNew= securityShiftsDao.findByShiftSlotBySecurityId(buildingSecurity.getSecurityId(),true);
			if(securityShiftsNew.size()==0)
			{
				listResponseSecurity.add(buildingSecurity);
			}
		}
		Date date= new Date();
		Instant inst = date.toInstant();
		 LocalDate localDate = inst.atZone(ZoneId.systemDefault()).toLocalDate();
		  Instant dayInst = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
		   Date day = Date.from(dayInst);
		  
		   Date dateTomorrow=DateUtils.addDays(day, 1);
		   dateTomorrow=DateUtils.addMinutes(dateTomorrow, -1);
		   System.out.println(dateTomorrow);
          
		  list=securityShiftsDao.findAllByendDateBetween(day,dateTomorrow);
		  
		  for(int i=0;i<list.size();i++)
			{
			  List<SecurityShifts> listNew= securityShiftsDao.findBySecurityAfterEndDate(list.get(i).getBuildingSecurity().getSecurityId(),list.get(i).getEndDate());
			  
			  System.out.println(listNew.size());
			  if(listNew.size()==0)
			  {
				 
				  listResponse.add(list.get(i));
				  
			  }
			}
		  for(SecurityShifts securityShift : listResponse)
		  {
			  listResponseSecurity.add(securityShift.getBuildingSecurity());
			  
		  }
		  HashMap<String, List<BuildingSecurity>> response = new HashMap<String,List<BuildingSecurity>>();
	         response.put("securitylist",listResponseSecurity);
		 return ResponseEntity.ok(response);
           
	}

	
}
