
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
import com.dizitiveit.pms.Dao.SlotsDao;
import com.dizitiveit.pms.Dao.UsersDao;
import com.dizitiveit.pms.Dao.VehicleMovementRegisterDao;
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
import com.dizitiveit.pms.model.Slots;
import com.dizitiveit.pms.model.Users;
import com.dizitiveit.pms.model.VehicleDetails;
import com.dizitiveit.pms.model.VehicleMovementRegister;
import com.dizitiveit.pms.model.VisitorParkingSlots;
import com.dizitiveit.pms.model.VisitorTagDetails;
import com.dizitiveit.pms.pojo.FlatSlotsPojo;
import com.dizitiveit.pms.pojo.FlatVisitorSlotPojo;
import com.dizitiveit.pms.pojo.SlotsPojo;
import com.dizitiveit.pms.pojo.VehicleMovementRegisterPojo;
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
	 
	 @Autowired
	 private SlotsDao slotsDao;
	 
	 @Autowired
	 private VehicleMovementRegisterDao vehicleMovementRegisterDao;
	
	@PostMapping(value="/saveVisitorDetails/{flatNo}")
	public ResponseEntity<?>  saveVisitorDetails(@PathVariable String flatNo,@RequestBody VisitorTagDetails visitorDetails,@RequestParam(name = "slotNo",required = false) String slotNo)
	{
		Users users = userDetailsService.getAuthUser();
		{
		Flats flats = flatsDao.findByflatNo(flatNo);
		 FlatOwners flatOwners = flatOwnersDao.findByownersActive(flats.getFlatId(),true);
		 if(flatOwners.getFlatResidencies() != null) {
			 visitorDetails.setResidentType("Tenant");
		 }
		 else {
			 visitorDetails.setResidentType("Owner");
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

		visitorDetails.setType("Unplanned Visitor");
		visitorDetails.setFlats(flats);
		visitorDetails.setVisitorStatus("PENDING");
		visitorDetails.setStatus(true);
		visitorDetails.setCreatedAt(new Date());
		System.out.println(visitorDetails.toString());
		visitorTagDetailsDao.save(visitorDetails);
		
	 
		}
		
	    //Users users = usersDao.findByFlats(flats);
		 flatOwners = flatOwnersDao.findByownersActive(flats.getFlatId(),true);
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
			visitorTagDetailsUpdate.setVisitorStatus("ACCEPTED");
			visitorTagDetailsUpdate.setBrand(visitorTagDetails.getBrand());
			visitorTagDetailsUpdate.setModel(visitorTagDetails.getModel());
			visitorTagDetailsUpdate.setVehicleNumber(visitorTagDetails.getVehicleNumber());
			visitorTagDetailsUpdate.setVehicleType(visitorTagDetails.getVehicleType());
			//VisitorParkingSlots visitorParkingSlots = visitorParkingSlotsDao.findBySlotNo(slotNo);
			Slots slots = slotsDao.findByslotNo(slotNo);
			if(slots.isFilled()==true)
			{
				Responses responses = responsesDao.findById(50);
				System.out.println("responseId" + responses.getResponsesId());
				System.out.println("resName" + responses.getResName());
				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName())); 
			}
			else
			{
				if(slots.getBillingType()==null)
				{	
				visitorTagDetailsUpdate.setSlots(slots);
				slots.setFilled(true);
				slots.setAssigned(true);
				//slots.setBillingType("Visitor");
				slots.setFlats(visitorTagDetailsUpdate.getFlats());
				
			slotsDao.save(slots);
				}
				else {
					visitorTagDetailsUpdate.setSlots(slots);
					slots.setFilled(true);
					slots.setFlats(visitorTagDetailsUpdate.getFlats());
				}
		} 
			visitorTagDetailsUpdate.setSlots(slots);
			visitorTagDetailsDao.save(visitorTagDetailsUpdate);
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
		visitorTagDetailsUpdate.setVisitorStatus("ACCEPTED");
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
		if(flats!=null)
		{
			 FlatOwners flatOwners = flatOwnersDao.findByownersActive(flats.getFlatId(),true);
			 if(flatOwners.getFlatResidencies()== null) {
				 visitorDetails.setResidentType("Owner");
			 }
			 else {
				 visitorDetails.setResidentType("Tenant");
			 }
		visitorDetails.setFlats(flats);
		visitorDetails.setType("Planned Visitor");
		visitorDetails.setVisitorStatus("PROCESSING");
		visitorDetails.setCreatedAt(new Date());
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
			pushnotificationreq.setTitle("There is a visitor request from this flat"+""+visitorDetails.getFlats().getFlatNo());
			pushnotificationreq.setMessage(visitorDetails.getPurpose());
			pushNotification.sendTokenNotification(pushnotificationreq);
				}
			
			}
			  flatOwners = flatOwnersDao.findByownersActive(flats.getFlatId(),true);
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
			 if(visitor.getSlots()!=null)
			 {
			 visitorDetailsPojo.setParkingSLot(visitor.getSlots().getSlotNo());
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
			 if(visitor.getSlots()!=null)
			 {
			 visitorDetailsPojo.setSlotNo(visitor.getSlots().getSlotNo());
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
			 if(visitor.getSlots()!=null)
			 {
			 visitorDetailsPojo.setParkingSLot(visitor.getSlots().getSlotNo());
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
	
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		  long diffInMillies = date2.getTime() - date1.getTime();
		  
		  return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS); 
		  }
	
	@PostMapping(value="/outVisitorDetails/{visitorId}")
	public ResponseEntity<?> outVisitorDetails(@PathVariable long visitorId){
		
		VisitorTagDetails visitorDetailsout = visitorTagDetailsDao.findByVisitorId(visitorId);
		if(visitorDetailsout!=null)
		{
		if(visitorDetailsout.getSlots()!=null)
		{
		visitorDetailsout.setOutTime(new Date());
		visitorDetailsout.setVisitorStatus("OUT");
		visitorDetailsout.setStatus(false);
		visitorDetailsout=visitorTagDetailsDao.save(visitorDetailsout);
		Slots slots = visitorDetailsout.getSlots();
		if(slots.getBillingType()!=null)
		{	
			slots.setFilled(false);
			slotsDao.save(slots);
		}
		else {
			slots.setFilled(false);
			slots.setAssigned(false);
			slots.setFlats(null);
			slotsDao.save(slots);
		VisitorTagDetails visitorOut = visitorTagDetailsDao.findByVisitorId(visitorId);
		long difference_In_Time  = visitorOut.getOutTime().getTime() - visitorOut.getInTime().getTime();
		System.out.println(visitorOut.getOutTime());
		System.out.println(visitorOut.getInTime());
		//long difference_In_Hours   = TimeUnit.MILLISECONDS.toHours(difference_In_Time) % 24; 
		// long difference_In_Hours = (difference_In_Time / (1000*60*60)) % 24; 
		  long hours = getDateDiff (visitorOut.getInTime(), visitorOut.getOutTime(), TimeUnit.HOURS);
		  if(visitorOut.getSlots().getBillingType()==null)
		  {
		 if(hours>=12) {
			 System.out.println("in if condition");
			 long diff_In_Days = Math.abs(visitorOut.getOutTime().getTime() - visitorOut.getInTime().getTime());
			 long diff = TimeUnit.DAYS.convert(diff_In_Days, TimeUnit.MILLISECONDS);
			 System.out.println(diff);
			 double dayCost = 500;
			 double cost= diff* dayCost;
			// System.out.println(cost);
			 visitorDetailsout.setParkingCost(cost);
			visitorTagDetailsDao.save(visitorDetailsout);
		 }
		 else if(hours>=3)
		 {
			 double actualCost = 50;	 
			 double cost= hours* actualCost;
			 visitorDetailsout.setParkingCost(cost);
			visitorTagDetailsDao.save(visitorDetailsout);
		 }
		 
		}
		}
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
		  List<VisitorTagDetails>list= visitorTagDetailsDao.getAllVisitorsStatus("PENDING","Unplanned Visitor");
		  for(int i=0;i<list.size();i++)
			{
			  
			 if(list.get(i).getCreatedAt().before(date))
			 {
				 VisitorTagDetails visitorTagDetails=list.get(i);
				 visitorTagDetails.setVisitorStatus("NOTANSWERED");
				 visitorTagDetailsDao.save(visitorTagDetails);
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
				pushnotificationreq.setTitle(visitorTagDetails.getFlats().getFlatNo()+""+"Request has been"+visitorTagDetails.getVisitorStatus());
				pushnotificationreq.setMessage(visitorTagDetails.getPurpose());
				pushNotification.sendTokenNotification(pushnotificationreq);
					}
				}
					}
			 
			 }
			  
	}
	
	@PostMapping(value="/updateStatus/{visitorId}/{visitorStatus}")
	public ResponseEntity<?> updateStatus(@PathVariable long visitorId,@PathVariable String visitorStatus)
	{
		Users users = userDetailsService.getAuthUser();
		VisitorTagDetails visitorDetails =  visitorTagDetailsDao.findByVisitorId(visitorId);
		//Users usersDetails = usersDao.findByMobile(users.getMobile());
		if(visitorStatus.equalsIgnoreCase("ACCEPT"))
		{
		visitorDetails.setVisitorStatus(visitorStatus);
		//visitorDetails.setInTime(new Date());
		visitorTagDetailsDao.save(visitorDetails);
		 String message = "Please use this Id as"+" "+visitorDetails.getVisitorId()+" "+" exist code at the gate"; 
		 otpService.sendSms(visitorDetails.getPhoneNumber(), message);
		}
		else {
			visitorDetails.setVisitorStatus(visitorStatus);
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
	pushnotificationreq.setTitle(visitorDetails.getFlats().getFlatNo()+""+"Request has been"+visitorDetails.getVisitorStatus());
	pushnotificationreq.setMessage(visitorDetails.getPurpose());
	pushNotification.sendTokenNotification(pushnotificationreq);
		}
	
	}
		Responses responses = responsesDao.findById(52);
		System.out.println("responseId" + responses.getResponsesId());
		System.out.println("resName" + responses.getResName());
		return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		
	}
	
	@PostMapping(value="changeStatus/{visitorId}")
	public ResponseEntity<?> changeStatus(@PathVariable long visitorId){
		VisitorTagDetails visitorDetails =  visitorTagDetailsDao.findByVisitorId(visitorId);
		if(visitorDetails.getVisitorStatus().equalsIgnoreCase("REJECT")) {
			visitorDetails.setVisitorStatus("REJECTED");
			visitorTagDetailsDao.save(visitorDetails);
		}
		else if(visitorDetails.getVisitorStatus().equalsIgnoreCase("NOT AT HOME"))
		{
			visitorDetails.setVisitorStatus("NOT AT HOUSE");
			visitorTagDetailsDao.save(visitorDetails);
		}
		else if(visitorDetails.getVisitorStatus().equalsIgnoreCase("NOTANSWERED")){
			visitorDetails.setVisitorStatus("NOTANSWER");
			visitorTagDetailsDao.save(visitorDetails);
		}
		else if(visitorDetails.getVisitorStatus().equalsIgnoreCase("PROCESSING")) {
			visitorDetails.setVisitorStatus("PENDING");
			visitorTagDetailsDao.save(visitorDetails);
		}
		Responses responses = responsesDao.findById(73);
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
     
     @GetMapping(value="/getVisitorsByType/{type}")
     public ResponseEntity<?>  getVisitorsByType(@PathVariable String type){
    	 List<VisitorTagDetails>listVisitorTagDetails= visitorTagDetailsDao.getVisitorDetailsBytype(type);
    	 HashMap<String, List<VisitorTagDetails>> response = new HashMap<String,List<VisitorTagDetails>>();
    	 response.put("listVisitorTagDetails",listVisitorTagDetails);
 		return ResponseEntity.ok(response);
     }
     
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
			 if(listVisitorTagDetails.getSlots()!=null)
			 {
			 visitorDetailsPojo.setParkingSLot(listVisitorTagDetails.getSlots().getSlotNo());
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
 		List<VisitorTagDetails> listVisitorTagDetails = visitorTagDetailsDao.getAllVisitorsStatus("ACCEPT","");
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
 			 if(visitor.getSlots()!=null)
 			 {
 			 visitorDetailsPojo.setParkingSLot(visitor.getSlots().getSlotNo());
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
    			 if(visitor.getSlots()!=null)
    			 {
    			 visitorDetailsPojo.setParkingSLot(visitor.getSlots().getSlotNo());
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
    		 String flatNo= value;  
    	     Flats flats = flatsDao.findByflatNo(flatNo);
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
    			 if(visitor.getSlots()!=null)
    			 {
    			 visitorDetailsPojo.setParkingSLot(visitor.getSlots().getSlotNo());
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
    	 else if(type.equalsIgnoreCase("Date")) {
    		 Date date;
    		 try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
				System.out.println(date);
				List<VisitorTagDetails> listVisitorTagDetails = visitorTagDetailsDao.findByVisitorDate(date,true);
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
	    			 if(visitor.getSlots()!=null)
	    			 {
	    			 visitorDetailsPojo.setParkingSLot(visitor.getSlots().getSlotNo());
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
   			 if(visitor.getSlots()!=null)
			 {
			 visitorDetailsPojo.setParkingSLot(visitor.getSlots().getSlotNo());
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
     
 	
 	@GetMapping("/currentVehiclesByFlat/{flatNo}")
 	public ResponseEntity<?> currentVehiclesByFlat(@PathVariable String flatNo)
 	{
 		Flats flats = flatsDao.findByflatNo(flatNo);
 		List<Slots> slotsList = slotsDao.getByflatNo(flats.getFlatId(),"Flat");
 		FlatVisitorSlotPojo flatVisitorSlotPojo = new FlatVisitorSlotPojo();
 		List<FlatSlotsPojo> flatSlotsPojoList = new ArrayList();
 		for(Slots slot : slotsList) 
 		{
 			List<VehicleMovementRegister> vehicleMovementRegisterList = vehicleMovementRegisterDao.findByVehicleDetails(slot.getSlotsId());
 			List<VehicleMovementRegisterPojo> vehicleMovementList = new ArrayList();
 			for(VehicleMovementRegister vehicle:vehicleMovementRegisterList)
 			{
 				VehicleMovementRegisterPojo vehicleMovement = new VehicleMovementRegisterPojo();
 				vehicleMovement.setFlatNo(vehicle.getVehicleDetails().getFlats().getFlatNo());
 				vehicleMovement.setRegNo(vehicle.getVehicleDetails().getRegNo());
 				vehicleMovement.setSlotNo(vehicle.getSlots().getSlotNo());
 				 DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
 	   			if(vehicle.getVehicleIn()!=null) {
 	   			vehicleMovement.setVehicleIn((dfIn.format(vehicle.getVehicleIn())));
 					}
 	   		 DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
 	   		if(vehicle.getVehicleOut()!=null) {
 	   			vehicleMovement.setVehicleOut((dfOut.format(vehicle.getVehicleOut())));
 					}
 				
 				vehicleMovement.setVehiclemovementId(vehicle.getVehiclemovementId());
 				vehicleMovementList.add(vehicleMovement);
 				
 			}
 			
 			FlatSlotsPojo flatsPojo = new FlatSlotsPojo();
 			flatsPojo.setVehicleMovementRegisterPojo(vehicleMovementList);

 			
 			List<VisitorTagDetails> vistorList = visitorTagDetailsDao.getByslot(slot.getSlotsId());
 			List<VisitorTagDetailsPojo> visitorListPojo = new ArrayList();
 			for( VisitorTagDetails visitor :  vistorList)
 			{
 				VisitorTagDetailsPojo visitorPojo = new VisitorTagDetailsPojo();
 				visitorPojo.setBlockNumber(visitor.getBlockNumber());
 				visitorPojo.setBrand(visitor.getBrand());
 				DateFormat dfExpectedIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if(visitor.getExpectedInTime()!=null) {
					visitorPojo.setExpectedInTime((dfExpectedIn.format(visitor.getExpectedInTime())));
				}
				DateFormat dfExpectedOut= new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if(visitor.getExpectedOutTime()!=null) {
					visitorPojo.setExpectedOutTime((dfExpectedOut.format(visitor.getExpectedOutTime())));
				}
 				visitorPojo.setFlatNo(flatNo);
 				visitorPojo.setModel(visitor.getModel());
 				 DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
 				if(visitor.getInTime()!=null) {
 					visitorPojo.setInTime((dfIn.format(visitor.getInTime())));
				}
	
				DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if(visitor.getOutTime()!=null) {
					visitorPojo.setOutTime((dfOut.format(visitor.getOutTime())));
				}
 				visitorPojo.setParkingCost(visitor.getParkingCost());
 				visitorPojo.setSlotNo(visitor.getSlots().getSlotNo());
 				visitorPojo.setPhoneNumber(visitor.getPhoneNumber());
 				visitorPojo.setPurpose(visitor.getPurpose());
 				visitorPojo.setResidentType(visitor.getResidentType());
 				visitorPojo.setVehicleNumber(visitor.getVehicleNumber());
 				visitorPojo.setVehicleType(visitor.getVehicleType());
 				visitorPojo.setVisitorId(visitor.getVisitorId());
 				visitorPojo.setVisitorName(visitor.getVisitorName());
 				visitorPojo.setVisitorStatus(visitor.getVisitorStatus());
 				
 				visitorListPojo.add(visitorPojo);
 				
 			}
 			
 			flatsPojo.setVisitorTagDetailsPojo(visitorListPojo);
 			flatSlotsPojoList.add(flatsPojo);
 		}
 		flatVisitorSlotPojo.setFlatsPojo(flatSlotsPojoList);
 		
 		List<Slots> slotsVisitorList = slotsDao.getByflatNo(flats.getFlatId(),"Visitor");
 		//System.out.println(slotsVisitorList.size());
 		//System.out.println(flats.getFlatId());
 		for(Slots slot : slotsVisitorList) 
 		{
 			List<VehicleMovementRegister> vehicleMovementRegisterList = vehicleMovementRegisterDao.findByVehicleDetails(slot.getSlotsId());
 			//System.out.println(slot.getSlotsId());
 			//System.out.println(vehicleMovementRegisterList.size());
 			List<VehicleMovementRegisterPojo> vehicleMovementList = new ArrayList();
 			for(VehicleMovementRegister vehicle:vehicleMovementRegisterList)
 			{
 				VehicleMovementRegisterPojo vehicleMovement = new VehicleMovementRegisterPojo();
 				vehicleMovement.setFlatNo(vehicle.getVehicleDetails().getFlats().getFlatNo());
 				vehicleMovement.setRegNo(vehicle.getVehicleDetails().getRegNo());
 				vehicleMovement.setSlotNo(vehicle.getSlots().getSlotNo());
 				 DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
 	   			if(vehicle.getVehicleIn()!=null) {
 	   			vehicleMovement.setVehicleIn((dfIn.format(vehicle.getVehicleIn())));
 					}
 	   		 DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
 	   		if(vehicle.getVehicleOut()!=null) {
 	   			vehicleMovement.setVehicleOut((dfOut.format(vehicle.getVehicleOut())));
 					}
 				
 				vehicleMovement.setVehiclemovementId(vehicle.getVehiclemovementId());
 				vehicleMovementList.add(vehicleMovement);
 				System.out.println(vehicleMovementList.size());
 			}
 			
 			FlatSlotsPojo flatsPojo = new FlatSlotsPojo();
 			flatsPojo.setVehicleMovementRegisterPojo(vehicleMovementList);

 			
 			List<VisitorTagDetails> vistorList = visitorTagDetailsDao.getByslot(slot.getSlotsId());
 			List<VisitorTagDetailsPojo> visitorListPojo = new ArrayList();
 			for( VisitorTagDetails visitor :  vistorList)
 			{
 				VisitorTagDetailsPojo visitorPojo = new VisitorTagDetailsPojo();
 				visitorPojo.setBlockNumber(visitor.getBlockNumber());
 				visitorPojo.setBrand(visitor.getBrand());
 				DateFormat dfExpectedIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if(visitor.getExpectedInTime()!=null) {
					visitorPojo.setExpectedInTime((dfExpectedIn.format(visitor.getExpectedInTime())));
				}
				DateFormat dfExpectedOut= new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if(visitor.getExpectedOutTime()!=null) {
					visitorPojo.setExpectedOutTime((dfExpectedOut.format(visitor.getExpectedOutTime())));
				}
 				visitorPojo.setFlatNo(flatNo);
 				visitorPojo.setModel(visitor.getModel());
 				 DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
 				if(visitor.getInTime()!=null) {
 					visitorPojo.setInTime((dfIn.format(visitor.getInTime())));
				}
	
				DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if(visitor.getOutTime()!=null) {
					visitorPojo.setOutTime((dfOut.format(visitor.getOutTime())));
				}
 				visitorPojo.setParkingCost(visitor.getParkingCost());
 				visitorPojo.setSlotNo(visitor.getSlots().getSlotNo());
 				visitorPojo.setPhoneNumber(visitor.getPhoneNumber());
 				visitorPojo.setPurpose(visitor.getPurpose());
 				visitorPojo.setResidentType(visitor.getResidentType());
 				visitorPojo.setVehicleNumber(visitor.getVehicleNumber());
 				visitorPojo.setVehicleType(visitor.getVehicleType());
 				visitorPojo.setVisitorId(visitor.getVisitorId());
 				visitorPojo.setVisitorName(visitor.getVisitorName());
 				visitorPojo.setVisitorStatus(visitor.getVisitorStatus());
 				
 				visitorListPojo.add(visitorPojo);
 				
 			}
 			flatsPojo.setVisitorTagDetailsPojo(visitorListPojo);
 			flatSlotsPojoList.add(flatsPojo);
 	
 	}
 		
 		flatVisitorSlotPojo.setVisitorPojo(flatSlotsPojoList);
 		
 		 HashMap<String,FlatVisitorSlotPojo> response = new HashMap<String,FlatVisitorSlotPojo>();
		 response.put("slots",flatVisitorSlotPojo);
		return ResponseEntity.ok(response);
 	}     
 	
 	@GetMapping("/getVisitorSlots")
 	public ResponseEntity<?> getVisitorSlots(){			
 		List<Slots>	emptySlotsList = slotsDao.findBySlots(false);
 		List<SlotsPojo> slotsPojoList = new ArrayList();
 		for(Slots slot : emptySlotsList) 
 		{
 			SlotsPojo slotsPojo = new SlotsPojo();
 			slotsPojo.setBlock(slot.getBlock());
 			if(slot.getFlats()!=null)
 			{	
 			slotsPojo.setFlatNo(slot.getFlats().getFlatNo());
 			}
 			slotsPojo.setOccupied(slot.isOccupied());
 			slotsPojo.setSlotNo(slot.getSlotNo());
 			slotsPojo.setVehicleType(slot.getVehicleType());
 			slotsPojo.setFilled(slot.isFilled());
 			slotsPojo.setAssigned(slot.isAssigned());
 			slotsPojo.setBillingType(slot.getBillingType());
 			slotsPojo.setFloor(slot.getFloor());
 			slotsPojoList.add(slotsPojo);
 		}
 		HashMap<String,List<SlotsPojo>> response = new HashMap<String,List<SlotsPojo>>();
		 response.put("listOfParkingSlots",slotsPojoList);
		return ResponseEntity.ok(response);
 	}
 	
	  @GetMapping("/getVisitorsInvoiceList/{flatNo}/{month}/{year}")
	  public ResponseEntity<?> getVisitorsInvoiceList(@PathVariable String flatNo,@PathVariable long month,@PathVariable long year ) {
		    Flats flats = flatsDao.findByflatNo(flatNo);
			List<VisitorTagDetails> vistorList = visitorTagDetailsDao.getVisitorInvoice(month, year,  flats.getFlatId(),"OUT");
 			List<VisitorTagDetailsPojo> visitorListPojo = new ArrayList();
 			for( VisitorTagDetails visitor :  vistorList)
 			{
 				VisitorTagDetailsPojo visitorPojo = new VisitorTagDetailsPojo();
 				visitorPojo.setBlockNumber(visitor.getBlockNumber());
 				visitorPojo.setBrand(visitor.getBrand());
 				DateFormat dfExpectedIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if(visitor.getExpectedInTime()!=null) {
					visitorPojo.setExpectedInTime((dfExpectedIn.format(visitor.getExpectedInTime())));
				}
				DateFormat dfExpectedOut= new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if(visitor.getExpectedOutTime()!=null) {
					visitorPojo.setExpectedOutTime((dfExpectedOut.format(visitor.getExpectedOutTime())));
				}
 				visitorPojo.setFlatNo(flatNo);
 				visitorPojo.setModel(visitor.getModel());
 				 DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
 				if(visitor.getInTime()!=null) {
 					visitorPojo.setInTime((dfIn.format(visitor.getInTime())));
				}
	
				DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if(visitor.getOutTime()!=null) {
					visitorPojo.setOutTime((dfOut.format(visitor.getOutTime())));
				}
 				visitorPojo.setParkingCost(visitor.getParkingCost());
 				if(visitor.getSlots()!=null)
 				{	
 				visitorPojo.setSlotNo(visitor.getSlots().getSlotNo());
 				}
 				visitorPojo.setPhoneNumber(visitor.getPhoneNumber());
 				visitorPojo.setPurpose(visitor.getPurpose());
 				visitorPojo.setResidentType(visitor.getResidentType());
 				visitorPojo.setVehicleNumber(visitor.getVehicleNumber());
 				visitorPojo.setVehicleType(visitor.getVehicleType());
 				visitorPojo.setVisitorId(visitor.getVisitorId());
 				visitorPojo.setVisitorName(visitor.getVisitorName());
 				visitorPojo.setVisitorStatus(visitor.getVisitorStatus());
 				visitorPojo.setType(visitor.getType());
 				visitorListPojo.add(visitorPojo);
 
 		}	
 			 HashMap<String, List<VisitorTagDetailsPojo>> response = new HashMap<String,List<VisitorTagDetailsPojo>>();
			 response.put("visitorListPojo",visitorListPojo);
			return ResponseEntity.ok(response);
	  }
	  
		
		
		 
}	