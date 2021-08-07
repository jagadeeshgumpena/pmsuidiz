package com.dizitiveit.pms.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.Dao.DailyVisitorShiftsDao;
import com.dizitiveit.pms.Dao.DailyVisitorsDao;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.model.DailyVisitors;
import com.dizitiveit.pms.model.DailyVisitorsShifts;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.SecurityShifts;
import com.dizitiveit.pms.pojo.DailyVisitorsPojo;
import com.dizitiveit.pms.pojo.FlatVisitorSlotPojo;

@RestController
@RequestMapping("/dailyVisitors")
public class DailyVisitorsController {

	@Autowired
	private ResponsesDao responsesDao;
	
	@Autowired
	private DailyVisitorsDao dailyVisitorsDao;
	
	@Autowired
	private FlatsDao flatsDao;
	
	@Autowired
	private DailyVisitorShiftsDao dailyVisitorsShiftsDao;
	
	@PostMapping("/saveDailyVisitors")
	public ResponseEntity<?> saveDailyVisitors(@RequestBody DailyVisitors dailyVisitorsNew){
		DailyVisitors dailyVisitors = dailyVisitorsDao.findByMobile(dailyVisitorsNew.getMobile());
		if(dailyVisitors!=null)
		{
			Responses responses = responsesDao.findById(14);
 			System.out.println("responseId" + responses.getResponsesId());
 			System.out.println("resName" + responses.getResName());
 			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		}
		else {
			dailyVisitorsDao.save(dailyVisitorsNew);
			Responses responses = responsesDao.findById(88);
 			System.out.println("responseId" + responses.getResponsesId());
 			System.out.println("resName" + responses.getResName());
 			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		}
	}
	
	@GetMapping("/getDailyVisitors")
	public ResponseEntity<?> getDailyVisitors(){
		List<DailyVisitors> dailyVisitors = dailyVisitorsDao.findAll();
		HashMap<String,List<DailyVisitors>> response = new HashMap<String,List<DailyVisitors>>();
		 response.put("dailyVisitors",dailyVisitors);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/getDailyVisitorsByflatNo/{flatNo}")
	public ResponseEntity<?> getDailyVisitorsByflatNo(@PathVariable String flatNo ){
		Flats flats = flatsDao.findByflatNo(flatNo);
		List<DailyVisitors> dailyVisitors = dailyVisitorsDao.findAll();
		List<DailyVisitorsPojo> dailyVisitorsPojoList = new ArrayList();
		for(DailyVisitors dailyVisitor : dailyVisitors )
		{
			DailyVisitorsPojo dailyVisitorsPojo = new DailyVisitorsPojo();
			dailyVisitorsPojo.setName(dailyVisitor.getName());
			dailyVisitorsPojo.setMobile(dailyVisitor.getMobile());
			dailyVisitorsPojo.setDailyVisitorId(dailyVisitor.getDailyVisitorId());
			dailyVisitorsPojo.setCategory(dailyVisitor.getCategory());
			dailyVisitorsPojo.setFlatNo(flatNo);
			DailyVisitorsShifts dailyVisitorsShifts = dailyVisitorsShiftsDao.getByActiveService(flats.getFlatId(), dailyVisitor.getDailyVisitorId(),true);
			if(dailyVisitorsShifts == null)
			{
				dailyVisitorsPojo.setAssigned(false);
			}
			else {
				dailyVisitorsPojo.setAssigned(true);
				dailyVisitorsPojo.setShift(dailyVisitorsShifts.getShiftTime());
				DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
				Date dateobj = dailyVisitorsShifts.getStartDate();
				System.out.println(df.format(dateobj));
				if(dailyVisitorsShifts.getStartDate()!=null) {
					dailyVisitorsPojo.setStartDate(df.format(dateobj));
				}
				
				Date dateEnd = dailyVisitorsShifts.getEndDate();
				System.out.println(df.format(dateEnd));
				if(dailyVisitorsShifts.getEndDate()!=null) {
					dailyVisitorsPojo.setEndDate(df.format(dateEnd));
				}

			}
			dailyVisitorsPojoList.add(dailyVisitorsPojo);
		}
		HashMap<String,List<DailyVisitorsPojo>> response = new HashMap<String,List<DailyVisitorsPojo>>();
		 response.put("dailyVisitors",dailyVisitorsPojoList);
		return ResponseEntity.ok(response);
	}
	
	@Scheduled(cron="0 0 6 * * *")
	public void DeactivatingSlots() throws IOException {
		
		Date date = new Date();
		 System.out.println(date);
		  List<DailyVisitorsShifts>list= dailyVisitorsShiftsDao.findByendDateLessThan(date);
		  for(int i=0;i<list.size();i++)
			{
			  
			  list.get(i).setActive(false);;
			  dailyVisitorsShiftsDao.save(list.get(i));
			  
			}
	}
	
	@GetMapping("/getDailyVisitorsToday")
	public ResponseEntity<?> getDailyVisitorsToday(){
	
		Date date = new Date();
		Instant inst = date.toInstant();
		 LocalDate localDate = inst.atZone(ZoneId.systemDefault()).toLocalDate();
		  Instant dayInst = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
		   Date day = Date.from(dayInst);
	     System.out.println(day);
	     Date dateToday=DateUtils.addMinutes(day, -1);
		List<DailyVisitorsShifts> dailyVisitors = dailyVisitorsShiftsDao.findByDailyVisitorsToday(day, true);
		List<DailyVisitorsPojo> dailyVisitorsPojoList = new ArrayList();
		
		for(DailyVisitorsShifts dailyVisitor : dailyVisitors )
		{
			DailyVisitorsPojo dailyVisitorsPojo = new DailyVisitorsPojo();
			dailyVisitorsPojo.setName(dailyVisitor.getDailyVisitors().getName());
			dailyVisitorsPojo.setMobile(dailyVisitor.getDailyVisitors().getMobile());
			dailyVisitorsPojo.setDailyVisitorId(dailyVisitor.getDailyVisitors().getDailyVisitorId());
			dailyVisitorsPojo.setFlatNo(dailyVisitor.getFlats().getFlatNo());
			dailyVisitorsPojo.setCategory(dailyVisitor.getDailyVisitors().getCategory());
			
				dailyVisitorsPojo.setShift(dailyVisitor.getShiftTime());
				DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
				Date dateobj = dailyVisitor.getStartDate();
				System.out.println(df.format(dateobj));
				if(dailyVisitor.getStartDate()!=null) {
					dailyVisitorsPojo.setStartDate(df.format(dateobj));
				}
				
				Date dateEnd = dailyVisitor.getEndDate();
				System.out.println(df.format(dateEnd));
				if(dailyVisitor.getEndDate()!=null) {
					dailyVisitorsPojo.setEndDate(df.format(dateEnd));
				}
				dailyVisitorsPojoList.add(dailyVisitorsPojo);
			}
			
		HashMap<String,List<DailyVisitorsPojo>> response = new HashMap<String,List<DailyVisitorsPojo>>();
		 response.put("dailyVisitors",dailyVisitorsPojoList);
		return ResponseEntity.ok(response);
	}
	
}
