package com.dizitiveit.pms.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;
import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.Dao.BuildingSecurityDao;
import com.dizitiveit.pms.Dao.FlatOwnersDao;
import com.dizitiveit.pms.Dao.FlatResidenciesDao;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.Dao.SecurityLoginDetailsDao;
import com.dizitiveit.pms.Dao.UsersDao;
import com.dizitiveit.pms.Dao.VisitorParkingSlotsDao;
import com.dizitiveit.pms.Dao.VisitorTagDetailsDao;
import com.dizitiveit.pms.model.BuildingSecurity;
import com.dizitiveit.pms.model.FlatOwners;
import com.dizitiveit.pms.model.FlatResidencies;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.PushNotificationRequest;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.SecurityLoginDetails;
import com.dizitiveit.pms.model.SecurityShifts;
import com.dizitiveit.pms.model.Users;
import com.dizitiveit.pms.model.VehicleDetails;
import com.dizitiveit.pms.model.VisitorParkingSlots;
import com.dizitiveit.pms.model.VisitorTagDetails;
import com.dizitiveit.pms.pojo.VisitorTagDetailsPojo;
import com.dizitiveit.pms.service.MyUserDetailsService;
import com.dizitiveit.pms.service.OtpSenderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping("/visitortag")
@RestController
public class VisitorTagDetailsController {
	
	@Autowired
	private FlatsDao flatsDao;
	
	@Autowired
	private VisitorTagDetailsDao visitorTagDetailsDao;
	
	@Autowired
	private ResponsesDao responsesDao;
	
	@Autowired
	private FlatOwnersDao flatOwnersDao;
	
	@Autowired
	private FlatResidenciesDao flatResidenciesDao;
	
	@Autowired
    private PushNotificationController pushNotification;
	
	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private BuildingSecurityDao buildingSecurityDao;
	
	@Autowired
	private VisitorParkingSlotsDao visitorParkingSlotsDao;
	
	 @Autowired
	   private SecurityLoginDetailsDao securityLoginDetailsDao;
	 
	 @Autowired
	 private OtpSenderService otpService;
	
	@PostMapping(value="/saveVisitorDetails/{flatNo}")
	public ResponseEntity<?>  saveVisitorDetails(@PathVariable String flatNo,@RequestBody VisitorTagDetails visitorDetails,@RequestParam(name = "slotNo",required = false) String slotNo)
	{
		Users users = userDetailsService.getAuthUser();
		{
		Flats flats = flatsDao.findByflatNo(flatNo);
		 FlatOwners flatOwnersvisitor = flatOwnersDao.findByownersActive(flats.getFlatId(),true);
		 if(flatOwnersvisitor.getFlatResidencies()==null) {
		 
			 visitorDetails.setResidentType("Owner");
		 }
		 else {
			 visitorDetails.setResidentType("Tenant");
		 }
		if(slotNo=="") {
			System.out.println("in unplanned");
		visitorDetails.setFlats(flats);
		visitorDetails.setType("Unplanned Visitor");
		visitorDetails.setCreatedAt(new Date());
		//visitorDetails.setOutTime(DateUtils.addMinutes(new Date(), +15));
		visitorDetails.setVisitorStatus("PENDING");
		visitorDetails.setStatus(true);
		System.out.println(visitorDetails.toString());
		visitorTagDetailsDao.save(visitorDetails);
		
		}
			else
		{

		VisitorParkingSlots visitorParkingSlots = visitorParkingSlotsDao.findBySlotNo(slotNo);
		System.out.println(slotNo);
		if(visitorParkingSlots.isSlotOccupied()==true)
		{
			Responses responses = responsesDao.findById(50);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName())); 
		}
		else
		{
			visitorDetails.setVisitorParkingSlots(visitorParkingSlots);
			visitorDetails.setType("Unplanned Visitor");
		visitorParkingSlots.setSlotOccupied(true);
		visitorDetails.setFlats(flats);
		visitorDetails.setVisitorStatus("PENDING");
		visitorDetails.setStatus(true);
		visitorDetails.setCreatedAt(new Date());
		visitorParkingSlotsDao.save(visitorParkingSlots);	
		System.out.println(visitorDetails.toString());
		visitorTagDetailsDao.save(visitorDetails);
		
	} 
		}
		
	    //Users users = usersDao.findByFlats(flats);
		 FlatOwners flatOwners = flatOwnersDao.findByownersActive(flats.getFlatId(),true);
		 if(flatOwners.getFlatResidencies()==null) {
			// FlatOwners flatOwnersToken =  flatOwnersDao.findByownersPhone(flatOwners.getPhone(),true);
			users = usersDao.findByMobile(flatOwners.getPhone());
			 System.out.println(users.toString());
			 System.out.println(flatOwners.getPhone());
		System.out.println(users.getToken());
		PushNotificationRequest pushnotificationreq = new PushNotificationRequest();
		pushnotificationreq.setToken(users.getToken());
		System.out.println(users.getToken());
		pushnotificationreq.setTitle("You had a Visitor");
		pushnotificationreq.setMessage(visitorDetails.getPurpose());
		pushNotification.sendTokenNotification(pushnotificationreq);
		 }
		 else {
			 FlatResidencies flatResidencies = flatResidenciesDao.findBytenantsActive(flats.getFlatId(),true);
			  users = usersDao.findByMobile(flatResidencies.getPhone());
			 System.out.println(users.getToken());
				PushNotificationRequest pushnotificationreq = new PushNotificationRequest();
				pushnotificationreq.setToken(users.getToken());
				System.out.println(users.getToken());
				pushnotificationreq.setTitle("You had a Visitor");
				pushnotificationreq.setMessage(visitorDetails.getPurpose());
				pushNotification.sendTokenNotification(pushnotificationreq);
		 }
		Responses responses = responsesDao.findById(22);
		System.out.println("responseId" + responses.getResponsesId());
		System.out.println("resName" + responses.getResName());
		return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
			
	}
		
	}	 
	
	@PostMapping("/savePlannedVisitorVehicle/{visitorId}/{slotNo}")
	public ResponseEntity<?> savePlannedVisitorVehicle(@PathVariable long visitorId,@RequestBody VisitorTagDetails  visitorTagDetails,@PathVariable String slotNo){
		Users users = userDetailsService.getAuthUser();
		System.out.println(users.toString());
		System.out.println(visitorTagDetails.toString());
		
		VisitorTagDetails visitorTagDetailsUpdate = visitorTagDetailsDao.findByVisitorId(visitorId);
		if(visitorTagDetails!=null) 
		{
			visitorTagDetailsUpdate.setInTime(new Date());
			visitorTagDetailsUpdate.setVisitorStatus("ACCEPT");
			visitorTagDetailsUpdate.setBrand(visitorTagDetails.getBrand());
			visitorTagDetailsUpdate.setModel(visitorTagDetails.getModel());
			visitorTagDetailsUpdate.setVehicleNumber(visitorTagDetails.getVehicleNumber());
			visitorTagDetailsUpdate.setVehicleType(visitorTagDetails.getVehicleType());
			VisitorParkingSlots visitorParkingSlots = visitorParkingSlotsDao.findBySlotNo(slotNo);
			if(visitorParkingSlots.isSlotOccupied()==true)
			{
				Responses responses = responsesDao.findById(50);
				System.out.println("responseId" + responses.getResponsesId());
				System.out.println("resName" + responses.getResName());
				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName())); 
			}
			else
			{
				visitorTagDetailsUpdate.setVisitorParkingSlots(visitorParkingSlots);
			visitorParkingSlots.setSlotOccupied(true);
			visitorParkingSlotsDao.save(visitorParkingSlots);	
			visitorTagDetailsDao.save(visitorTagDetailsUpdate);
		} 
		}
		Responses responses = responsesDao.findById(49);
		System.out.println("responseId" + responses.getResponsesId());
		System.out.println("resName" + responses.getResName());
		return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName())); 
	}
	
	@PostMapping("/saveInTime/{visitorId}")
	public ResponseEntity<?> saveInTime(@PathVariable long visitorId){
		VisitorTagDetails visitorTagDetailsUpdate = visitorTagDetailsDao.findByVisitorId(visitorId);
		visitorTagDetailsUpdate.setInTime(new Date());
		visitorTagDetailsUpdate.setStatus(true);
		visitorTagDetailsUpdate.setVisitorStatus("ACCEPT");
		visitorTagDetailsDao.save(visitorTagDetailsUpdate);
		Responses responses = responsesDao.findById(49);
		System.out.println("responseId" + responses.getResponsesId());
		System.out.println("resName" + responses.getResName());
		return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
	}
	
	@PostMapping("/reRequest/{visitorId}")
	public ResponseEntity<?> reRequest(@PathVariable long visitorId){
		
		VisitorTagDetails visitorTagDetailsUpdate = visitorTagDetailsDao.findByVisitorId(visitorId);
		FlatOwners flatOwners = flatOwnersDao.findByownersActive(visitorTagDetailsUpdate.getFlats().getFlatId(),true);
		
		 if(flatOwners.getFlatResidencies()==null) {
		
				// FlatOwners flatOwnersToken =  flatOwnersDao.findByownersPhone(flatOwners.getPhone(),true);
		      Users  users = usersDao.findByMobile(flatOwners.getPhone());
				 System.out.println(users.toString());
				 System.out.println(flatOwners.getPhone());
			System.out.println(users.getToken());
			PushNotificationRequest pushnotificationreq = new PushNotificationRequest();
			pushnotificationreq.setToken(users.getToken());
			System.out.println(users.getToken());
			pushnotificationreq.setTitle("You had a Visitor");
			pushnotificationreq.setMessage("Your Visitor is late should I allow the visitor");
			pushNotification.sendTokenNotification(pushnotificationreq);
			 }
			 else {
				 FlatResidencies flatResidencies = flatResidenciesDao.findBytenantsActive(visitorTagDetailsUpdate.getFlats().getFlatId(),true);
				Users  users = usersDao.findByMobile(flatResidencies.getPhone());
				 System.out.println(users.getToken());
					PushNotificationRequest pushnotificationreq = new PushNotificationRequest();
					pushnotificationreq.setToken(users.getToken());
					System.out.println(users.getToken());
					pushnotificationreq.setTitle("You had a Visitor");
					pushnotificationreq.setMessage("Your Visitor is late should I allow the visitor");
					pushNotification.sendTokenNotification(pushnotificationreq);
			 }
		 Responses responses = responsesDao.findById(52);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
				
	}
	
	@PostMapping("/savePlannedVisitors/{flatNo}")
	public ResponseEntity<?> savePlannedVisitors(@PathVariable String flatNo,@RequestBody VisitorTagDetails visitorDetails){
		Users users = userDetailsService.getAuthUser();
		System.out.println(users.toString());
		if(users.getRoles().equalsIgnoreCase("ROLE_OWNER")||users.getRoles().equalsIgnoreCase("ROLE_TENANT")) 
		{
		Flats flats = flatsDao.findByflatNo(flatNo);
		FlatOwners flatOwnersvisitor = flatOwnersDao.findByownersActive(flats.getFlatId(),true);
		 if(flatOwnersvisitor.getFlatResidencies()==null) {
		 
			 visitorDetails.setResidentType("Owner");
		 }
		 else {
			 visitorDetails.setResidentType("Tenant");
		 }
		if(flats!=null)
		{
		visitorDetails.setFlats(flats);
		visitorDetails.setType("Planned Visitor");
		visitorDetails.setVisitorStatus("PENDING");
		visitorDetails.setStatus(true);
		System.out.println(visitorDetails.toString());
		visitorTagDetailsDao.save(visitorDetails);
		List<SecurityLoginDetails> listSecurityLoginDetails = securityLoginDetailsDao.findByPurpose("IN");
 			List<Users> userslist = usersDao.findByRoles("ROLE_SECURITY", true);
			for(SecurityLoginDetails securityLoginDetails : listSecurityLoginDetails)
			{
				BuildingSecurity buildingSecurity = securityLoginDetails.getBuildingSecurity();
				Users user = usersDao.findByMobile(buildingSecurity.getMobile());
				if(user.getToken()!=null) {
			PushNotificationRequest pushnotificationreq = new PushNotificationRequest();
			pushnotificationreq.setToken(user.getToken());
			System.out.println(user.getToken());
			pushnotificationreq.setTitle("You had a Visitor");
			pushnotificationreq.setMessage(visitorDetails.getPurpose());
			pushNotification.sendTokenNotification(pushnotificationreq);
				}
			
			}
			 FlatOwners flatOwners = flatOwnersDao.findByownersActive(flats.getFlatId(),true);
			 if(flatOwners.getFlatResidencies()==null) {
				// FlatOwners flatOwnersToken =  flatOwnersDao.findByownersPhone(flatOwners.getPhone(),true);
				users = usersDao.findByMobile(flatOwners.getPhone());
				 System.out.println(users.toString());
				 System.out.println(flatOwners.getPhone());
				 DateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");  
				    String strDate = formatter.format(visitorDetails.getExpectedInTime());  
				    System.out.println("Date Format with dd MMMM yyyy : "+strDate);  
				 String message = flatOwners.getFirstname()+" "+flatOwners.getLastName()+" "+"has invited you to Block"+" "+flatOwners.getFlats().getBlock()+" "+flatOwners.getFlats().getTower()+" "+flatOwners.getFlats().getFlatNo()+" "+"on"+" "+strDate+" "+"using PMS.Please Use"+" "+visitorDetails.getVisitorId()+" "+"as entry code at the gate"; 
				 otpService.sendSms(visitorDetails.getPhoneNumber(), message);
			 }
			 else {
				 FlatResidencies flatResidencies = flatResidenciesDao.findBytenantsActive(flats.getFlatId(),true);
				  users = usersDao.findByMobile(flatResidencies.getPhone());
				 DateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");  
				    String strDate = formatter.format(visitorDetails.getExpectedInTime());  
				    System.out.println("Date Format with dd MMMM yyyy : "+strDate);  
				 String message = flatResidencies.getFirstname()+" "+flatResidencies.getLastName()+" "+"has invited you to Block"+" "+flatResidencies.getFlats().getBlock()+" "+flatResidencies.getFlats().getTower()+" "+flatResidencies.getFlats().getFlatNo()+" "+"on"+" "+strDate+" "+"using PMS.Please Use"+" "+visitorDetails.getVisitorId()+" "+"as entry code at the gate";
				 System.out.println(visitorDetails.getPhoneNumber());
				 otpService.sendSms(visitorDetails.getPhoneNumber(), message);
			 }
		Responses responses = responsesDao.findById(22);
		System.out.println("responseId" + responses.getResponsesId());
		System.out.println("resName" + responses.getResName());
		return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		
		}
		else {
			Responses responses = responsesDao.findById(37);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		}
		}
		else {
			return ResponseEntity.ok("Not An Authorized User"); 
		}
		}

	
	 
	@GetMapping(value="/getVisitorDetailsByStatus/{visitorStatus}")
	public ResponseEntity<?> getVisitorDetails(@PathVariable String visitorStatus){
		 List<VisitorTagDetailsPojo> listVisitorTagDetailsPojo = new ArrayList();
		 List<VisitorTagDetails> listVisitorTagDetails=visitorTagDetailsDao.findByVisitorStatus(visitorStatus);
		 for(VisitorTagDetails visitor : listVisitorTagDetails) {
			 VisitorTagDetailsPojo visitorDetailsPojo = new VisitorTagDetailsPojo();
			 visitorDetailsPojo.setVisitorId(visitor.getVisitorId());
			 visitorDetailsPojo.setVisitorName(visitor.getVisitorName());
			 visitorDetailsPojo.setBlockNumber(visitor.getBlockNumber());
			 visitorDetailsPojo.setBrand(visitor.getBrand());
			 visitorDetailsPojo.setPhoneNumber(visitor.getPhoneNumber());
			 visitorDetailsPojo.setVehicleType(visitor.getVehicleType());
			 DateFormat dfExpectedIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			 if(visitor.getExpectedInTime()!=null) {
					visitorDetailsPojo.setExpectedInTime((dfExpectedIn.format(visitor.getExpectedInTime())));
				}
				DateFormat dfExpectedOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				if(visitor.getExpectedOutTime()!=null) {
					visitorDetailsPojo.setExpectedOutTime((dfExpectedOut.format(visitor.getExpectedOutTime())));
				}
			 visitorDetailsPojo.setFlatNo(visitor.getFlats().getFlatNo());
			 visitorDetailsPojo.setModel(visitor.getModel());
			 DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				if(visitor.getInTime()!=null) {
					visitorDetailsPojo.setInTime((dfIn.format(visitor.getInTime())));
				}
	
				DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				if(visitor.getOutTime()!=null) {
					visitorDetailsPojo.setOutTime((dfOut.format(visitor.getOutTime())));
				}
			 visitorDetailsPojo.setParkingCost(visitor.getParkingCost());
			 if(visitor.getVisitorParkingSlots()!=null)
			 {
			 visitorDetailsPojo.setParkingSLot(visitor.getVisitorParkingSlots().getSlotNo());
			 }
			 visitorDetailsPojo.setPurpose(visitor.getPurpose());
			 visitorDetailsPojo.setStatus(visitor.isStatus());
			 visitorDetailsPojo.setType(visitor.getType());
			 visitorDetailsPojo.setVehicleNumber(visitor.getVehicleNumber());
			 visitorDetailsPojo.setVisitorStatus(visitor.getVisitorStatus());
			 listVisitorTagDetailsPojo.add(visitorDetailsPojo);
		 }
		HashMap<String, List<VisitorTagDetailsPojo>> response = new HashMap<String,List<VisitorTagDetailsPojo>>();
		Date test = DateUtils.addMinutes(new Date(), +15);
		 System.out.println(test);
		response.put("listVisitorTagDetails",listVisitorTagDetailsPojo);
		return ResponseEntity.ok(response);
		 
            
	}
	
	@GetMapping(value="/getVisitorDetailsByVehicleNumber/{vehicleNumber}")
	public ResponseEntity<?> getVisitorDetailsByVehicleId(@PathVariable String vehicleNumber){
		 
		List<VisitorTagDetails> listVisitorTagDetails = visitorTagDetailsDao.findByvehicleNumber(vehicleNumber);
		HashMap<String, List<VisitorTagDetails>> response = new HashMap<String,List<VisitorTagDetails>>();
		response.put("listVisitorTagDetails",listVisitorTagDetails);
        return ResponseEntity.ok(response);
		
	}
	
	@GetMapping(value="listOfVisitors")
	public ResponseEntity<?> listOfVisitors(){
		 List<VisitorTagDetailsPojo> listVisitorTagDetailsPojo = new ArrayList();
		 List<VisitorTagDetails> listVisitorTagDetails = visitorTagDetailsDao.findAllActiveVisitors(true);
		 for(VisitorTagDetails visitor : listVisitorTagDetails) {
			 VisitorTagDetailsPojo visitorDetailsPojo = new VisitorTagDetailsPojo();
			 visitorDetailsPojo.setVisitorId(visitor.getVisitorId());
			 visitorDetailsPojo.setVisitorName(visitor.getVisitorName());
			 visitorDetailsPojo.setBlockNumber(visitor.getBlockNumber());
			 visitorDetailsPojo.setBrand(visitor.getBrand());
			 visitorDetailsPojo.setPhoneNumber(visitor.getPhoneNumber());
			 visitorDetailsPojo.setVehicleType(visitor.getVehicleType());
			 DateFormat dfExpectedIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			 if(visitor.getExpectedInTime()!=null) {
					visitorDetailsPojo.setExpectedInTime((dfExpectedIn.format(visitor.getExpectedInTime())));
				}
				DateFormat dfExpectedOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				if(visitor.getExpectedOutTime()!=null) {
					visitorDetailsPojo.setExpectedOutTime((dfExpectedOut.format(visitor.getExpectedOutTime())));
				}
			 visitorDetailsPojo.setFlatNo(visitor.getFlats().getFlatNo());
			 visitorDetailsPojo.setModel(visitor.getModel());
			 DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				if(visitor.getInTime()!=null) {
					visitorDetailsPojo.setInTime((dfIn.format(visitor.getInTime())));
				}
	
				DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				if(visitor.getOutTime()!=null) {
					visitorDetailsPojo.setOutTime((dfOut.format(visitor.getOutTime())));
				}
			 visitorDetailsPojo.setParkingCost(visitor.getParkingCost());
			 if(visitor.getVisitorParkingSlots()!=null)
			 {
			 visitorDetailsPojo.setParkingSLot(visitor.getVisitorParkingSlots().getSlotNo());
			 }
			 visitorDetailsPojo.setPurpose(visitor.getPurpose());
			 visitorDetailsPojo.setStatus(visitor.isStatus());
			 visitorDetailsPojo.setType(visitor.getType());
			 visitorDetailsPojo.setVehicleNumber(visitor.getVehicleNumber());
			 visitorDetailsPojo.setVisitorStatus(visitor.getVisitorStatus());
			 listVisitorTagDetailsPojo.add(visitorDetailsPojo);
		 }
		HashMap<String, List<VisitorTagDetailsPojo>> response = new HashMap<String,List<VisitorTagDetailsPojo>>();
		response.put("listVisitorTagDetails",listVisitorTagDetailsPojo);
		return ResponseEntity.ok(response);
	}
	

	@GetMapping("/listOfOutVisitorDetails/{flatNo}")
	public ResponseEntity<?> listOfOutVisitorDetails(@PathVariable String flatNo){
		 List<VisitorTagDetailsPojo> listVisitorTagDetailsPojo = new ArrayList();
			Flats flats = flatsDao.findByflatNo(flatNo);
		List<VisitorTagDetails> listVisitorTagDetails = visitorTagDetailsDao.findAllOutVisitors(false, flats.getFlatId());
		 for(VisitorTagDetails visitor : listVisitorTagDetails) {
			 VisitorTagDetailsPojo visitorDetailsPojo = new VisitorTagDetailsPojo();
			 visitorDetailsPojo.setVisitorId(visitor.getVisitorId());
			 visitorDetailsPojo.setVisitorName(visitor.getVisitorName());
			 visitorDetailsPojo.setBlockNumber(visitor.getBlockNumber());
			 visitorDetailsPojo.setBrand(visitor.getBrand());
			 visitorDetailsPojo.setPhoneNumber(visitor.getPhoneNumber());
			 visitorDetailsPojo.setVehicleType(visitor.getVehicleType());
			 DateFormat dfExpectedIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 if(visitor.getExpectedInTime()!=null) {
					visitorDetailsPojo.setExpectedInTime((dfExpectedIn.format(visitor.getExpectedInTime())));
				}
				DateFormat dfExpectedOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(visitor.getExpectedOutTime()!=null) {
					visitorDetailsPojo.setExpectedOutTime((dfExpectedOut.format(visitor.getExpectedOutTime())));
				}
			 visitorDetailsPojo.setFlatNo(visitor.getFlats().getFlatNo());
			 visitorDetailsPojo.setModel(visitor.getModel());
			 DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(visitor.getInTime()!=null) {
					visitorDetailsPojo.setInTime((dfIn.format(visitor.getInTime())));
				}
	
				DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(visitor.getOutTime()!=null) {
					visitorDetailsPojo.setOutTime((dfOut.format(visitor.getOutTime())));
				}
			 visitorDetailsPojo.setParkingCost(visitor.getParkingCost());
			 if(visitor.getVisitorParkingSlots()!=null)
			 {
			 visitorDetailsPojo.setParkingSLot(visitor.getVisitorParkingSlots().getSlotNo());
			 }
			 visitorDetailsPojo.setPurpose(visitor.getPurpose());
			 visitorDetailsPojo.setStatus(visitor.isStatus());
			 visitorDetailsPojo.setType(visitor.getType());
			 visitorDetailsPojo.setVehicleNumber(visitor.getVehicleNumber());
			 visitorDetailsPojo.setVisitorStatus(visitor.getVisitorStatus());
			 listVisitorTagDetailsPojo.add(visitorDetailsPojo);
		 }
		HashMap<String, List<VisitorTagDetailsPojo>> response = new HashMap<String,List<VisitorTagDetailsPojo>>();
		response.put("listVisitorTagDetails",listVisitorTagDetailsPojo);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(value="/outVisitorDetails/{visitorId}")
	public ResponseEntity<?> outVisitorDetails(@PathVariable long visitorId){
		
		VisitorTagDetails visitorDetailsout = visitorTagDetailsDao.findByVisitorId(visitorId);
		if(visitorDetailsout!=null)
		{
		if(visitorDetailsout.getVisitorParkingSlots()!=null)
		{
		visitorDetailsout.setOutTime(new Date());
		visitorDetailsout.setVisitorStatus("OUT");
		visitorDetailsout.setStatus(false);
		visitorDetailsout=visitorTagDetailsDao.save(visitorDetailsout);
		VisitorParkingSlots visitorParkingSlots = visitorDetailsout.getVisitorParkingSlots();
		visitorParkingSlots.setSlotOccupied(false);
		visitorParkingSlotsDao.save(visitorParkingSlots);
		long difference_In_Time  = visitorDetailsout.getOutTime().getTime() - visitorDetailsout.getInTime().getTime();
		
		 long difference_In_Hours   = TimeUnit.MILLISECONDS.toHours(difference_In_Time) % 24; 
		 if(difference_In_Hours<1)
		 {
			 difference_In_Hours=1;
		 }
		 
		 double cost= difference_In_Hours* visitorDetailsout.getVisitorParkingSlots().getSlotCostPerHour();
		 System.out.println(cost);
		 visitorDetailsout.setParkingCost(cost);
		visitorTagDetailsDao.save(visitorDetailsout);
		//long diff = visitorDetailsout.getOutTime().getHours()- visitorDetailsout.getInTime().getHours();
		System.out.println(difference_In_Hours);
		Responses responses = responsesDao.findById(23);
		System.out.println("responseId" + responses.getResponsesId());
		System.out.println("resName" + responses.getResName());
		return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName())); 
		}
		else {
			visitorDetailsout.setOutTime(new Date());
			visitorDetailsout.setVisitorStatus("OUT");
			visitorDetailsout.setStatus(false);
			visitorDetailsout=visitorTagDetailsDao.save(visitorDetailsout);
			
			Responses responses = responsesDao.findById(23);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName())); 
			
		}
		}
		else {
			 Responses responses = responsesDao.findById(56);
				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		}
		 
	}

	/*
	 * @PostMapping("/changingStatus/{visitorId}") public ResponseEntity<?>
	 * changingStatus(@PathVariable long visitorId){
	 * 
	 * 
	 * }
	 */
	
	@Scheduled(cron = "0 */1 * * * *")
	public void ChangingStatus() throws IOException {
		Date date = new Date();
		date=DateUtils.addMinutes(date, -10);
		 System.out.println(date);
		  List<VisitorTagDetails>list= visitorTagDetailsDao.findAllVisitorsStatus("PENDING");
		  for(int i=0;i<list.size();i++)
			{
			  
			 if(list.get(i).getCreatedAt().before(date))
			 {
				 VisitorTagDetails visitorTagDetails=list.get(i);
				 visitorTagDetails.setVisitorStatus("NOTANSWERED");
				 visitorTagDetailsDao.save(visitorTagDetails);
			 }
			  
			}

	}
	
	@PostMapping(value="/updateStatus/{visitorId}/{visitorStatus}")
	public ResponseEntity<?> updateStatus(@PathVariable long visitorId,@PathVariable String visitorStatus){
		Users users = userDetailsService.getAuthUser();
		VisitorTagDetails visitorDetails =  visitorTagDetailsDao.findByVisitorId(visitorId);
		//Users usersDetails = usersDao.findByMobile(users.getMobile());
		if(visitorStatus.equalsIgnoreCase("ACCEPT"))
		{
		visitorDetails.setVisitorStatus(visitorStatus);
		visitorDetails.setInTime(new Date());
		visitorTagDetailsDao.save(visitorDetails);
		}
		else {
			visitorDetails.setVisitorStatus(visitorStatus);
			visitorTagDetailsDao.save(visitorDetails);
		}
		Responses responses = responsesDao.findById(52);
		System.out.println("responseId" + responses.getResponsesId());
		System.out.println("resName" + responses.getResName());
		return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName())); 
	}
	
	@GetMapping(value="getVisitorDetailsByflatNo/{flatNo}")
	public ResponseEntity<?> getVisitorDetailsByflatNo(@PathVariable String flatNo){
		Flats flats = flatsDao.findByflatNo(flatNo);		
		List<VisitorTagDetails>listVisitorTagDetails= visitorTagDetailsDao.getVisitorDetailsByflatNo("PENDING",flats.getFlatId(),"Unplanned Visitor");
		// HashMap<String, List<VisitorTagDetails>> response = new HashMap<String,List<VisitorTagDetails>>();
          //   response.put("listVisitorTagDetails",listVisitorTagDetails);
		List<VisitorTagDetailsPojo>listVisitorTagDetailsPojo= new ArrayList();
		for(VisitorTagDetails visitor : listVisitorTagDetails) {
			 VisitorTagDetailsPojo visitorDetailsPojo = new VisitorTagDetailsPojo();
			 visitorDetailsPojo.setVisitorId(visitor.getVisitorId());
			 visitorDetailsPojo.setVisitorName(visitor.getVisitorName());
			 visitorDetailsPojo.setBlockNumber(visitor.getBlockNumber());
			 visitorDetailsPojo.setBrand(visitor.getBrand());
			 visitorDetailsPojo.setModel(visitor.getModel());
			 visitorDetailsPojo.setPhoneNumber(visitor.getPhoneNumber());
			 visitorDetailsPojo.setVehicleType(visitor.getVehicleType());
			 visitorDetailsPojo.setResidentType(visitor.getResidentType());
			 DateFormat dfExpectedIn = new SimpleDateFormat("dd-MM-yy'T'HH:mm:ss");
				if(visitor.getExpectedInTime()!=null) {
					visitorDetailsPojo.setExpectedInTime(dfExpectedIn.format(visitor.getExpectedInTime()));
				}
				 DateFormat dfExpectedOut = new SimpleDateFormat("dd-MM-yy'T'HH:mm:ss");
					if(visitor.getExpectedOutTime()!=null) {
						visitorDetailsPojo.setExpectedOutTime(dfExpectedOut.format(visitor.getExpectedOutTime()));
					}
					DateFormat dfIn = new SimpleDateFormat("dd-MM-yy'T'HH:mm:ss");
					if(visitor.getInTime()!=null) {
						visitorDetailsPojo.setInTime(dfIn.format(visitor.getInTime()));
					}
					DateFormat dfOut = new SimpleDateFormat("dd-MM-yy'T'HH:mm:ss");
					if(visitor.getOutTime()!=null) {
						visitorDetailsPojo.setOutTime(dfIn.format(visitor.getOutTime()));
					}
					DateFormat dfCreatedAt = new SimpleDateFormat("dd-MM-yy'T'HH:mm:ss");
					if(visitor.getCreatedAt()!=null) {
						visitorDetailsPojo.setCreatedAt(dfCreatedAt.format(visitor.getCreatedAt()));
					}
				 visitorDetailsPojo.setPurpose(visitor.getPurpose());
				 visitorDetailsPojo.setStatus(visitor.isStatus());
				 visitorDetailsPojo.setType(visitor.getType());
				 visitorDetailsPojo.setVehicleNumber(visitor.getVehicleNumber());
				 visitorDetailsPojo.setVisitorStatus(visitor.getVisitorStatus());
				 listVisitorTagDetailsPojo.add(visitorDetailsPojo);
		}
		HashMap<String, List<VisitorTagDetailsPojo>> response = new HashMap<String,List<VisitorTagDetailsPojo>>();
          response.put("listVisitorTagDetails",listVisitorTagDetailsPojo);
             return ResponseEntity.ok(response);
	}
	
     @GetMapping(value="getAllVisitorsByflatNo/{flatNo}")
     public ResponseEntity<?> getAllVisitorsByflatNo(@PathVariable String flatNo){
    	 Flats flats = flatsDao.findByflatNo(flatNo);
    	 List<VisitorTagDetails>listVisitorTagDetails= visitorTagDetailsDao.findByflats(flats);
    	 List<VisitorTagDetailsPojo>listVisitorTagDetailsPojo= new ArrayList();
 		for(VisitorTagDetails visitor : listVisitorTagDetails) {
 			 VisitorTagDetailsPojo visitorDetailsPojo = new VisitorTagDetailsPojo();
 			 visitorDetailsPojo.setVisitorId(visitor.getVisitorId());
 			 visitorDetailsPojo.setVisitorName(visitor.getVisitorName());
 			 visitorDetailsPojo.setBlockNumber(visitor.getBlockNumber());
 			 visitorDetailsPojo.setBrand(visitor.getBrand());
 			 visitorDetailsPojo.setPhoneNumber(visitor.getPhoneNumber());
 			 visitorDetailsPojo.setVehicleType(visitor.getVehicleType());
 			visitorDetailsPojo.setModel(visitor.getModel());
 			 DateFormat dfExpectedIn = new SimpleDateFormat("dd-MM-yy'T'HH:mm:ss");
 				if(visitor.getExpectedInTime()!=null) {
 					visitorDetailsPojo.setExpectedInTime(dfExpectedIn.format(visitor.getExpectedInTime()));
 				}
 				 DateFormat dfExpectedOut = new SimpleDateFormat("dd-MM-yy'T'HH:mm:ss");
 					if(visitor.getExpectedOutTime()!=null) {
 						visitorDetailsPojo.setExpectedOutTime(dfExpectedOut.format(visitor.getExpectedOutTime()));
 					}
 					DateFormat dfIn = new SimpleDateFormat("dd-MM-yy'T'HH:mm:ss");
 					if(visitor.getInTime()!=null) {
 						visitorDetailsPojo.setInTime(dfIn.format(visitor.getInTime()));
 					}
 					DateFormat dfOut = new SimpleDateFormat("dd-MM-yy'T'HH:mm:ss");
 					if(visitor.getOutTime()!=null) {
 						visitorDetailsPojo.setOutTime(dfIn.format(visitor.getOutTime()));
 					}
 					DateFormat dfCreatedAt = new SimpleDateFormat("dd-MM-yy'T'HH:mm:ss");
 					if(visitor.getCreatedAt()!=null) {
 						visitorDetailsPojo.setCreatedAt(dfCreatedAt.format(visitor.getCreatedAt()));
 					}
 				 visitorDetailsPojo.setPurpose(visitor.getPurpose());
 				 visitorDetailsPojo.setStatus(visitor.isStatus());
 				 visitorDetailsPojo.setType(visitor.getType());
 				 visitorDetailsPojo.setVehicleNumber(visitor.getVehicleNumber());
 				 visitorDetailsPojo.setVisitorStatus(visitor.getVisitorStatus());
 				visitorDetailsPojo.setResidentType(visitor.getResidentType());
 				 listVisitorTagDetailsPojo.add(visitorDetailsPojo);
 		}
 		HashMap<String, List<VisitorTagDetailsPojo>> response = new HashMap<String,List<VisitorTagDetailsPojo>>();
           response.put("listVisitorTagDetails",listVisitorTagDetailsPojo);
              return ResponseEntity.ok(response);
     }
     
     //@GetMapping(value="/getVisitorsByType/{type}")
    // public ResponseEntity<?>  getVisitorsByType(@PathVariable String type){
    	// List<VisitorTagDetails>listVisitorTagDetails= visitorTagDetailsDao.getVisitorDetailsBytype(type,true);
    	// HashMap<String, List<VisitorTagDetails>> response = new HashMap<String,List<VisitorTagDetails>>();
    	 //response.put("listVisitorTagDetails",listVisitorTagDetails);
 		//return ResponseEntity.ok(response);
     //}
     
     @GetMapping("/getVisitorDetailsByVisitorId/{visitorId}")
     public ResponseEntity<?> getVisitorDetailsByVisitorId(@PathVariable long visitorId){
    	 VisitorTagDetails listVisitorTagDetails = visitorTagDetailsDao.findByVisitorIdOrderByVisitorIdAsc(visitorId);		
			 VisitorTagDetailsPojo visitorDetailsPojo = new VisitorTagDetailsPojo();
			 visitorDetailsPojo.setVisitorId(listVisitorTagDetails.getVisitorId());
			 visitorDetailsPojo.setVisitorName(listVisitorTagDetails.getVisitorName());
			 visitorDetailsPojo.setBlockNumber(listVisitorTagDetails.getBlockNumber());
			 visitorDetailsPojo.setBrand(listVisitorTagDetails.getBrand());
			 visitorDetailsPojo.setPhoneNumber(listVisitorTagDetails.getPhoneNumber());
			 visitorDetailsPojo.setVehicleType(listVisitorTagDetails.getVehicleType());
			 DateFormat dfExpectedIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if(listVisitorTagDetails.getExpectedInTime()!=null) {
					visitorDetailsPojo.setExpectedInTime((dfExpectedIn.format(listVisitorTagDetails.getExpectedInTime())));
				}
				DateFormat dfExpectedOut = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if(listVisitorTagDetails.getExpectedOutTime()!=null) {
					visitorDetailsPojo.setExpectedOutTime((dfExpectedOut.format(listVisitorTagDetails.getExpectedOutTime())));
				}
			 visitorDetailsPojo.setFlatNo(listVisitorTagDetails.getFlats().getFlatNo());
			 visitorDetailsPojo.setModel(listVisitorTagDetails.getModel());
			 DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if(listVisitorTagDetails.getInTime()!=null) {
					visitorDetailsPojo.setInTime((dfIn.format(listVisitorTagDetails.getInTime())));
				}
	
				DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if(listVisitorTagDetails.getOutTime()!=null) {
					visitorDetailsPojo.setOutTime((dfOut.format(listVisitorTagDetails.getOutTime())));
				}
			 visitorDetailsPojo.setParkingCost(listVisitorTagDetails.getParkingCost());
			 if(listVisitorTagDetails.getVisitorParkingSlots()!=null)
			 {
			 visitorDetailsPojo.setParkingSLot(listVisitorTagDetails.getVisitorParkingSlots().getSlotNo());
			 }
			 visitorDetailsPojo.setPurpose(listVisitorTagDetails.getPurpose());
			 visitorDetailsPojo.setStatus(listVisitorTagDetails.isStatus());
			 visitorDetailsPojo.setType(listVisitorTagDetails.getType());
			 visitorDetailsPojo.setVehicleNumber(listVisitorTagDetails.getVehicleNumber());
			 visitorDetailsPojo.setVisitorStatus(listVisitorTagDetails.getVisitorStatus());
		 
		HashMap<String, VisitorTagDetailsPojo> response = new HashMap<String,VisitorTagDetailsPojo>();
		response.put("VisitorTagDetails",visitorDetailsPojo);
		return ResponseEntity.ok(response);
     }
     
     @GetMapping("/activeVisitorList")
     public ResponseEntity<?> activeVisitorList(){
    	 List<VisitorTagDetailsPojo> listVisitorTagDetailsPojo = new ArrayList();
 		List<VisitorTagDetails> listVisitorTagDetails = visitorTagDetailsDao.findAllVisitorsStatus("ACCEPT");
 		 for(VisitorTagDetails visitor : listVisitorTagDetails) {
 			 VisitorTagDetailsPojo visitorDetailsPojo = new VisitorTagDetailsPojo();
 			 visitorDetailsPojo.setVisitorId(visitor.getVisitorId());
 			 visitorDetailsPojo.setVisitorName(visitor.getVisitorName());
 			 visitorDetailsPojo.setBlockNumber(visitor.getBlockNumber());
 			 visitorDetailsPojo.setBrand(visitor.getBrand());
 			 visitorDetailsPojo.setPhoneNumber(visitor.getPhoneNumber());
 			 visitorDetailsPojo.setVehicleType(visitor.getVehicleType());
 			 DateFormat dfExpectedIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
 			 if(visitor.getExpectedInTime()!=null) {
 					visitorDetailsPojo.setExpectedInTime((dfExpectedIn.format(visitor.getExpectedInTime())));
 				}
 				DateFormat dfExpectedOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
 				if(visitor.getExpectedOutTime()!=null) {
 					visitorDetailsPojo.setExpectedOutTime((dfExpectedOut.format(visitor.getExpectedOutTime())));
 				}
 			 visitorDetailsPojo.setFlatNo(visitor.getFlats().getFlatNo());
 			 visitorDetailsPojo.setModel(visitor.getModel());
 			 DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
 				if(visitor.getInTime()!=null) {
 					visitorDetailsPojo.setInTime((dfIn.format(visitor.getInTime())));
 				}
 	
 				DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
 				if(visitor.getOutTime()!=null) {
 					visitorDetailsPojo.setOutTime((dfOut.format(visitor.getOutTime())));
 				}
 			 visitorDetailsPojo.setParkingCost(visitor.getParkingCost());
 			 if(visitor.getVisitorParkingSlots()!=null)
 			 {
 			 visitorDetailsPojo.setParkingSLot(visitor.getVisitorParkingSlots().getSlotNo());
 			 }
 			 visitorDetailsPojo.setPurpose(visitor.getPurpose());
 			 visitorDetailsPojo.setStatus(visitor.isStatus());
 			 visitorDetailsPojo.setType(visitor.getType());
 			 visitorDetailsPojo.setVehicleNumber(visitor.getVehicleNumber());
 			 visitorDetailsPojo.setVisitorStatus(visitor.getVisitorStatus());
 			 listVisitorTagDetailsPojo.add(visitorDetailsPojo);
 		 }
 		HashMap<String, List<VisitorTagDetailsPojo>> response = new HashMap<String,List<VisitorTagDetailsPojo>>();
 		response.put("listVisitorTagDetails",listVisitorTagDetailsPojo);
 		return ResponseEntity.ok(response);

     }
     
     
     @GetMapping("getVisitorDetails/{type}/{value}")
 	public ResponseEntity<?> getVisitorDetails(@PathVariable String type,@PathVariable String value) throws ParseException {
    	 List<VisitorTagDetailsPojo> listVisitorTagDetailsPojo = new ArrayList();
     	 if(type.equalsIgnoreCase("Visitors"))
 		{
    		 List<VisitorTagDetails>listVisitorTagDetails= visitorTagDetailsDao.getVisitorDetailsBytype(value);
    		 for(VisitorTagDetails visitor : listVisitorTagDetails) {
    			 VisitorTagDetailsPojo visitorDetailsPojo = new VisitorTagDetailsPojo();
    			 visitorDetailsPojo.setVisitorId(visitor.getVisitorId());
    			 visitorDetailsPojo.setVisitorName(visitor.getVisitorName());
    			 visitorDetailsPojo.setBlockNumber(visitor.getBlockNumber());
    			 visitorDetailsPojo.setPhoneNumber(visitor.getPhoneNumber());
    			 visitorDetailsPojo.setVehicleType(visitor.getVehicleType());
    			 visitorDetailsPojo.setBrand(visitor.getBrand());
    			 DateFormat dfExpectedIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
 				if(visitor.getExpectedInTime()!=null) {
 					visitorDetailsPojo.setExpectedInTime((dfExpectedIn.format(visitor.getExpectedInTime())));
 				}
 				DateFormat dfExpectedOut = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
 				if(visitor.getExpectedOutTime()!=null) {
 					visitorDetailsPojo.setExpectedOutTime((dfExpectedOut.format(visitor.getExpectedOutTime())));
 				}
    		
    			 visitorDetailsPojo.setFlatNo(visitor.getFlats().getFlatNo());
    			 visitorDetailsPojo.setModel(visitor.getModel());
    			 DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
 				if(visitor.getInTime()!=null) {
 					visitorDetailsPojo.setInTime((dfIn.format(visitor.getInTime())));
 				}
 	
 				DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
 				if(visitor.getOutTime()!=null) {
 					visitorDetailsPojo.setOutTime((dfOut.format(visitor.getOutTime())));
 				}
    			 visitorDetailsPojo.setParkingCost(visitor.getParkingCost());
    			 if(visitor.getVisitorParkingSlots()!=null)
    			 {
    			 visitorDetailsPojo.setParkingSLot(visitor.getVisitorParkingSlots().getSlotNo());
    			 }
    			 visitorDetailsPojo.setPurpose(visitor.getPurpose());
    			 visitorDetailsPojo.setStatus(visitor.isStatus());
    			 visitorDetailsPojo.setType(visitor.getType());
    			 visitorDetailsPojo.setVehicleNumber(visitor.getVehicleNumber());
    			 visitorDetailsPojo.setVisitorStatus(visitor.getVisitorStatus());
    			 listVisitorTagDetailsPojo.add(visitorDetailsPojo);
    		 }
    		 
        	 HashMap<String, List<VisitorTagDetailsPojo>> response = new HashMap<String,List<VisitorTagDetailsPojo>>();
        	 response.put("listVisitorTagDetails",listVisitorTagDetailsPojo);
     		return ResponseEntity.ok(response);
 		}
    	 else if(type.equalsIgnoreCase("flatNo")) {
    		
    	     Flats flats = flatsDao.findByflatNo(value);
        	 List<VisitorTagDetails>listVisitorTagDetails= visitorTagDetailsDao.getVisitorDetailsByflatNo(flats.getFlatId(),true);
        	 
        	 for(VisitorTagDetails visitor : listVisitorTagDetails) {
    			 VisitorTagDetailsPojo visitorDetailsPojo = new VisitorTagDetailsPojo();
    			 visitorDetailsPojo.setVisitorId(visitor.getVisitorId());
    			 visitorDetailsPojo.setVisitorName(visitor.getVisitorName());
    			 visitorDetailsPojo.setBlockNumber(visitor.getBlockNumber());
    			 visitorDetailsPojo.setPhoneNumber(visitor.getPhoneNumber());
    			 visitorDetailsPojo.setVehicleType(visitor.getVehicleType());
    			 visitorDetailsPojo.setBrand(visitor.getBrand());
    			 DateFormat dfExpectedIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
  				if(visitor.getExpectedInTime()!=null) {
  					visitorDetailsPojo.setExpectedInTime((dfExpectedIn.format(visitor.getExpectedInTime())));
  				}
  				DateFormat dfExpectedOut = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
  				if(visitor.getExpectedOutTime()!=null) {
  					visitorDetailsPojo.setExpectedOutTime((dfExpectedOut.format(visitor.getExpectedOutTime())));
  				}
    			 visitorDetailsPojo.setFlatNo(visitor.getFlats().getFlatNo());
    			 visitorDetailsPojo.setModel(visitor.getModel());
    			 DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
  				if(visitor.getInTime()!=null) {
  					visitorDetailsPojo.setInTime((dfIn.format(visitor.getInTime())));
  				}
  	
  				DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
  				if(visitor.getOutTime()!=null) {
  					visitorDetailsPojo.setOutTime((dfOut.format(visitor.getOutTime())));
  				}
    			 visitorDetailsPojo.setParkingCost(visitor.getParkingCost());
    			 if(visitor.getVisitorParkingSlots()!=null)
    			 {
    			 visitorDetailsPojo.setParkingSLot(visitor.getVisitorParkingSlots().getSlotNo());
    			 }
    			 visitorDetailsPojo.setPurpose(visitor.getPurpose());
    			 visitorDetailsPojo.setStatus(visitor.isStatus());
    			 visitorDetailsPojo.setType(visitor.getType());
    			 visitorDetailsPojo.setVehicleNumber(visitor.getVehicleNumber());
    			 visitorDetailsPojo.setVisitorStatus(visitor.getVisitorStatus());
    			 visitorDetailsPojo.setResidentType(visitor.getResidentType());
    			 listVisitorTagDetailsPojo.add(visitorDetailsPojo);
    		 }
    		 
        	 
        	 
        	 HashMap<String, List<VisitorTagDetailsPojo>> response = new HashMap<String,List<VisitorTagDetailsPojo>>();
        	 response.put("listVisitorTagDetails",listVisitorTagDetailsPojo);
     		return ResponseEntity.ok(response);
    		 
    	 }
    	 else if(type.equalsIgnoreCase("Date")) {
    		 Date date;
    		 try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
				Date dateTomorrow=DateUtils.addHours(date,23);
				dateTomorrow=DateUtils.addMinutes(dateTomorrow,59);
				   dateTomorrow=DateUtils.addSeconds(dateTomorrow, 59);
				System.out.println(dateTomorrow);
				List<VisitorTagDetails> listVisitorTagDetails = visitorTagDetailsDao.findByVisitorDate(dateTomorrow,true);
				for(VisitorTagDetails visitor : listVisitorTagDetails) {
	    			 VisitorTagDetailsPojo visitorDetailsPojo = new VisitorTagDetailsPojo();
	    			 visitorDetailsPojo.setVisitorId(visitor.getVisitorId());
	    			 visitorDetailsPojo.setVisitorName(visitor.getVisitorName());
	    			 visitorDetailsPojo.setBlockNumber(visitor.getBlockNumber());
	    			 visitorDetailsPojo.setPhoneNumber(visitor.getPhoneNumber());
	    			 visitorDetailsPojo.setVehicleType(visitor.getVehicleType());
	    			 visitorDetailsPojo.setBrand(visitor.getBrand());
	    			 DateFormat dfExpectedIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	   				if(visitor.getExpectedInTime()!=null) {
	   					visitorDetailsPojo.setExpectedInTime((dfExpectedIn.format(visitor.getExpectedInTime())));
	   				}
	   				DateFormat dfExpectedOut = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	   				if(visitor.getExpectedOutTime()!=null) {
	   					visitorDetailsPojo.setExpectedOutTime((dfExpectedOut.format(visitor.getExpectedOutTime())));
	   				}

	    			 visitorDetailsPojo.setFlatNo(visitor.getFlats().getFlatNo());
	    			 visitorDetailsPojo.setModel(visitor.getModel());
	    			 DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	  				if(visitor.getInTime()!=null) {
	  					visitorDetailsPojo.setInTime((dfIn.format(visitor.getInTime())));
	  				}
	  	
	  				DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	  				if(visitor.getOutTime()!=null) {
	  					visitorDetailsPojo.setOutTime((dfOut.format(visitor.getOutTime())));
	  				}
	    			 visitorDetailsPojo.setParkingCost(visitor.getParkingCost());
	    			 if(visitor.getVisitorParkingSlots()!=null)
	    			 {
	    			 visitorDetailsPojo.setParkingSLot(visitor.getVisitorParkingSlots().getSlotNo());
	    			 }
	    			 visitorDetailsPojo.setPurpose(visitor.getPurpose());
	    			 visitorDetailsPojo.setStatus(visitor.isStatus());
	    			 visitorDetailsPojo.setType(visitor.getType());
	    			 visitorDetailsPojo.setVehicleNumber(visitor.getVehicleNumber());
	    			 visitorDetailsPojo.setVisitorStatus(visitor.getVisitorStatus());
	    			 listVisitorTagDetailsPojo.add(visitorDetailsPojo);
	    		 }
	    		 
				 HashMap<String, List<VisitorTagDetailsPojo>> response = new HashMap<String,List<VisitorTagDetailsPojo>>();
	        	 response.put("listVisitorTagDetails",listVisitorTagDetailsPojo);
	     		return ResponseEntity.ok(response);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	 }
    	 else if(type.equalsIgnoreCase("today")) {
    		 Date date;
    		 date = new Date();
			System.out.println(date);
			List<VisitorTagDetails> listVisitorTagDetails = visitorTagDetailsDao.findByVisitorDate(date,true);
			for(VisitorTagDetails visitor : listVisitorTagDetails) {
   			 VisitorTagDetailsPojo visitorDetailsPojo = new VisitorTagDetailsPojo();
   			 visitorDetailsPojo.setVisitorId(visitor.getVisitorId());
   			 visitorDetailsPojo.setVisitorName(visitor.getVisitorName());
   			 visitorDetailsPojo.setBlockNumber(visitor.getBlockNumber());
   			 visitorDetailsPojo.setBrand(visitor.getBrand());
   			 DateFormat dfExpectedIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if(visitor.getExpectedInTime()!=null) {
					visitorDetailsPojo.setExpectedInTime((dfExpectedIn.format(visitor.getExpectedInTime())));
				}
				DateFormat dfExpectedOut = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if(visitor.getExpectedOutTime()!=null) {
					visitorDetailsPojo.setExpectedOutTime((dfExpectedOut.format(visitor.getExpectedOutTime())));
				}
   			 visitorDetailsPojo.setFlatNo(visitor.getFlats().getFlatNo());
   			 visitorDetailsPojo.setModel(visitor.getModel());
   			 DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
   			if(visitor.getInTime()!=null) {
					visitorDetailsPojo.setInTime((dfIn.format(visitor.getInTime())));
				}
	
				DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if(visitor.getOutTime()!=null) {
					visitorDetailsPojo.setOutTime((dfOut.format(visitor.getOutTime())));
				}
   			 visitorDetailsPojo.setParkingCost(visitor.getParkingCost());
   			 if(visitor.getVisitorParkingSlots()!=null)
			 {
			 visitorDetailsPojo.setParkingSLot(visitor.getVisitorParkingSlots().getSlotNo());
			 }
   			 visitorDetailsPojo.setPurpose(visitor.getPurpose());
   			 visitorDetailsPojo.setStatus(visitor.isStatus());
   			 visitorDetailsPojo.setType(visitor.getType());
   			 visitorDetailsPojo.setVehicleNumber(visitor.getVehicleNumber());
   			 visitorDetailsPojo.setVisitorStatus(visitor.getVisitorStatus());
   			 listVisitorTagDetailsPojo.add(visitorDetailsPojo);
   		 }
   		 
			 HashMap<String, List<VisitorTagDetailsPojo>> response = new HashMap<String,List<VisitorTagDetailsPojo>>();
			 response.put("listVisitorTagDetails",listVisitorTagDetailsPojo);
			return ResponseEntity.ok(response);
    	 }
    	  return null;
     }
   
     @PostMapping("/saveDailyVisitors/{flatNo}/{visitingDays}/{duration}")
 	public ResponseEntity<?> saveDailyVisitors(@PathVariable String flatNo,@RequestBody VisitorTagDetails visitorDetails,@PathVariable String visitingDays,@PathVariable String duration )
     {
    	 Users users = userDetailsService.getAuthUser();
    	 if(users.getRoles().equalsIgnoreCase("ROLE_OWNER")||users.getRoles().equalsIgnoreCase("ROLE_TENANT")) 
 		{
 		Flats flats = flatsDao.findByflatNo(flatNo);
 		if(flats!=null)
 		{
 			if(visitingDays.equalsIgnoreCase("Once"))
 			{
 				if(duration.equalsIgnoreCase("1hour"))
 				{
 					Date date = new Date();
 					date=DateUtils.addHours(visitorDetails.getExpectedInTime(), +1);
 					 System.out.println(date);
 			 		visitorDetails.setExpectedOutTime(date);
 				}
 				else if(duration.equalsIgnoreCase("3hours"))
 				{
 					Date date = new Date();
 					date=DateUtils.addHours(visitorDetails.getExpectedInTime(), +3);
 					 System.out.println(date);
 			 		visitorDetails.setExpectedOutTime(date);
 				}
 				else if(duration.equalsIgnoreCase("5hours"))
 				{
 					Date date = new Date();
 					date=DateUtils.addHours(visitorDetails.getExpectedInTime(), +5);
 					 System.out.println(date);
 			 		visitorDetails.setExpectedOutTime(date);
 				}
 				else if(duration.equalsIgnoreCase("7hours")) {
 					Date date = new Date();
 					date=DateUtils.addHours(visitorDetails.getExpectedInTime(), +7);
 					 System.out.println(date);
 			 		visitorDetails.setExpectedOutTime(date);
 				}
 				else if(duration.equalsIgnoreCase("10hours")) {
 					Date date = new Date();
 					date=DateUtils.addHours(visitorDetails.getExpectedInTime(), +10);
 					 System.out.println(date);
 			 		visitorDetails.setExpectedOutTime(date);
 				}
 		visitorDetails.setFlats(flats);
 		visitorDetails.setType("Daily Visitor");
 		visitorDetails.setVisitorStatus("PENDING");
 		visitorDetails.setStatus(true);
 		System.out.println(visitorDetails.toString());
 		visitorTagDetailsDao.save(visitorDetails);
 			}
 			else if(visitingDays.equalsIgnoreCase("Frequently")) {
 				visitorDetails.setFlats(flats);
 		 		visitorDetails.setType("Daily Visitor");
 		 		visitorDetails.setVisitorStatus("PENDING");
 		 		visitorDetails.setStatus(true);
 		 		System.out.println(visitorDetails.toString());
 		 		visitorTagDetailsDao.save(visitorDetails);
 			}
 		List<SecurityLoginDetails> listSecurityLoginDetails = securityLoginDetailsDao.findByPurpose("IN");
			List<Users> userslist = usersDao.findByRoles("ROLE_SECURITY", true);
		for(SecurityLoginDetails securityLoginDetails : listSecurityLoginDetails)
		{
			BuildingSecurity buildingSecurity = securityLoginDetails.getBuildingSecurity();
			Users user = usersDao.findByMobile(buildingSecurity.getMobile());
			if(user.getToken()!=null) {
		PushNotificationRequest pushnotificationreq = new PushNotificationRequest();
		pushnotificationreq.setToken(user.getToken());
		System.out.println(user.getToken());
		pushnotificationreq.setTitle("You had a Visitor");
		pushnotificationreq.setMessage(visitorDetails.getPurpose());
		pushNotification.sendTokenNotification(pushnotificationreq);
			}
		}
	
	Responses responses = responsesDao.findById(22);
	System.out.println("responseId" + responses.getResponsesId());
	System.out.println("resName" + responses.getResName());
	return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
	}
 		else {
 			Responses responses = responsesDao.findById(37);
 			System.out.println("responseId" + responses.getResponsesId());
 			System.out.println("resName" + responses.getResName());
 			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
 		}
 		}
 		else {
 			return ResponseEntity.ok("Not An Authorized User"); 
 		}
 		}
     
 	
 	
     

}	
     
