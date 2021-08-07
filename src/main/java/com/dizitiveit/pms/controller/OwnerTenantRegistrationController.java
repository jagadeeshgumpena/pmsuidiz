package com.dizitiveit.pms.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.Dao.AdditionalParkingSlotsDao;
import com.dizitiveit.pms.Dao.BuildingSecurityDao;
import com.dizitiveit.pms.Dao.FlatDetailsDao;
import com.dizitiveit.pms.Dao.FlatOwnersDao;
import com.dizitiveit.pms.Dao.FlatResidenciesDao;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.Dao.SlotsDao;
import com.dizitiveit.pms.Dao.UsersDao;
import com.dizitiveit.pms.Dao.VehicleDetailsDao;
import com.dizitiveit.pms.model.AdditionalParkingSlots;
import com.dizitiveit.pms.model.BuildingSecurity;
import com.dizitiveit.pms.model.FlatDetails;
import com.dizitiveit.pms.model.FlatOwners;
import com.dizitiveit.pms.model.FlatResidencies;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.Residents;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.Slots;
import com.dizitiveit.pms.model.Users;
import com.dizitiveit.pms.model.VehicleDetails;
import com.dizitiveit.pms.pojo.ActiveResidentDetailsPojo;
import com.dizitiveit.pms.pojo.FlatDetailsPojo;
import com.dizitiveit.pms.pojo.FlatOwnersPojo;
import com.dizitiveit.pms.pojo.FlatResidenciesPojo;
import com.dizitiveit.pms.pojo.SecurityShiftsPojo;
import com.dizitiveit.pms.pojo.SlotsPojo;
import com.dizitiveit.pms.pojo.VehicleDetailsPojo;
import com.dizitiveit.pms.service.MyUserDetailsService;
import com.dizitiveit.pms.service.OtpSenderService;

@RestController
@RequestMapping("/ownerTenant")
public class OwnerTenantRegistrationController {

	@Autowired
	private ResponsesDao responsesDao;

	@Autowired
	private FlatResidenciesDao flatResidenciesDao;

   @Autowired
   private FlatOwnersDao flatOwnersDao;
   
   @Autowired
   private BuildingSecurityDao buildingSecurityDao;
	
   @Autowired
   private FlatDetailsDao flatDetailsDao;
   
   @Autowired
	private FlatsDao flatsDao;
   
   @Autowired
	private UsersDao usersDao;
   
   @Autowired
	private MyUserDetailsService userDetailsService;
   
   @Autowired
   private VehicleDetailsDao vehicleDetailsDao;
   
   @Autowired
	 private OtpSenderService otpService;
   
   @Autowired
   private AdditionalParkingSlotsDao additionalParkingSlotsDao;
   
   @Autowired
   private SlotsDao slotsDao;

	@Transactional
	@RequestMapping(value = "/ownerRegistration/{flatNo}/{type}", method = RequestMethod.POST)
	public ResponseEntity<?> signUp(@RequestBody FlatOwners flatOwners,@PathVariable String flatNo,@PathVariable String type) {
		Flats flats = flatsDao.findByflatNo(flatNo);
		
		if(flats!=null)
		{
		if(type.equalsIgnoreCase("Owner"))
		{
			FlatOwners flatOwnersActive = flatOwnersDao.findByownersActive(flats.getFlatId(), true);
			if(flatOwnersActive==null)
			{
				
		Users users= new Users();
		String email = flatOwners.getEmail();
		System.out.println(flatOwners.getEmail());
		String mobile = flatOwners.getPhone();
	    String firstName = flatOwners.getFirstname();
		String lastName = flatOwners.getLastName();
       String roles = "ROLE_OWNER";
		String password = "";
		Users users1 = new Users();
		users1 = usersDao.findByEmail(email);
		if(users1!=null)
		{
			 Responses responses = responsesDao.findById(13);
			 	  System.out.println("responseId"+responses.getResponsesId());
			  	  System.out.println("resName"+responses.getResName());
			   		  
			 return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName())); 
		}
		
	    users1 = usersDao.findByMobile(mobile);
	    if(users1!=null)
		{

			 Responses responses = responsesDao.findById(14);
			 //	  System.out.println("responseId"+responses.getResponsesId());
			  //	  System.out.println("resName"+responses.getResName());
			   		  
			 return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName())); 
		}
		users1 = new Users();
		// System.out.println("Inside sendOtp mobile:"+user.toString());
		//if (users1 == null) {
			users1.setEmail(email);
			users1.setMobile(mobile);
			users1.setFirstName(firstName);
			users1.setLastName(lastName);
			users1.setRoles(roles);
			users1.setMobileVerification(true);
			users1.setActive(true);
			users1.setPassword(password);
			System.out.println(users1.toString());
			long userId = userDetailsService.registerNewUser(users1);	
		
			//if(flatOwners.getFlatDetails()!=null)
			//{
				
			
				flatOwners.setFlats(flats);
				flatOwners.setOwnerActive(true);	
				flatOwners.setType("Owner");
				flatOwners.setCreatedAt(new Date());
				System.out.println(flatOwners);
				flatOwnersDao.save(flatOwners);	
				 String message = flatOwners.getFirstname()+" "+flatOwners.getLastName()+" "+"your registered to the flat"+" "+flatOwners.getFlats().getFlatNo()+" "+"with the mobile number"+" "+flatOwners.getPhone()+" "+"Please download the app and login into it"; 
				 otpService.sendSms(flatOwners.getPhone(), message);
				Responses responses = responsesDao.findById(81);
	   			  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
			
			//}
			
			//sendEmail(email);
			//FlatResidencies flatResidencies = residents.getFlatResidencies();
			
				
			}
			else
			{
				Responses responses = responsesDao.findById(24);
	   			  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
			}
			
		
		
	}
	
	
		else 
		{
			System.out.println(flats.toString());
			FlatOwners flatOwnersActive = flatOwnersDao.findByownersActive(flats.getFlatId(), true);
			System.out.println("in else method");
			if(flatOwnersActive==null)
			{
				
				flatOwners.setFlats(flats);
				flatOwners.setOwnerActive(true);
				flatOwners.setType("Management");
				flatOwners.setCreatedAt(new Date());
				flatOwnersDao.save(flatOwners);	
				
				 Responses responses = responsesDao.findById(29);
		   		  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
			}
			
			
			//sendEmail(email);
			//FlatResidencies flatResidencies = residents.getFlatResidencies();
			 
			
			else
			{
				Responses responses = responsesDao.findById(24);
	   			  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
			}
		}
}
		
		else
		{
			Responses responses = responsesDao.findById(37);
   			  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
		}	
		
			
}
			
	
  		@PostMapping("/updateOwnerDetails/{flatNo}")
  		 public ResponseEntity<?> updateOwnerDetails(@PathVariable String flatNo,@RequestBody FlatOwners flatOwners){
  	
  			Flats flats = flatsDao.findByflatNo(flatNo);
  			FlatOwners flatOwnersUpdate = flatOwnersDao.findByownersActiveAndType(flats.getFlatId(), true, "Management");
  			flatOwnersUpdate.setFirstname(flatOwners.getFirstname());
  			flatOwnersUpdate.setLastName(flatOwners.getLastName());
  			flatOwnersUpdate.setEmail(flatOwners.getEmail());
  			flatOwnersUpdate.setPhone(flatOwners.getPhone());
  			flatOwnersUpdate.setType("Owner");
  			flatOwnersDao.save(flatOwnersUpdate);
  			Users usersUpdate = new Users();
  			usersUpdate.setActive(true);
  			usersUpdate.setFirstName(flatOwners.getFirstname());
  			usersUpdate.setLastName(flatOwners.getLastName());
  			usersUpdate.setEmail(flatOwners.getEmail());
  			usersUpdate.setMobile(flatOwners.getPhone());
  			usersUpdate.setMobileVerification(true);
  			usersUpdate.setRoles("ROLE_OWNER");
  			usersUpdate.setCreatedAt(new Date());
  			usersUpdate.setPassword("");
  			usersDao.save(usersUpdate);
  			Responses responses = responsesDao.findById(30);
 			  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
  		}
		
	@Transactional
	@RequestMapping(value = "/tenantRegistartion/{flatNo}", method = RequestMethod.POST)
	 public ResponseEntity<?> tenentRegistartion(@PathVariable String flatNo,@RequestBody FlatResidencies flatResidencies){
		System.out.println(flatResidencies.toString());
		Flats flats = flatsDao.findByflatNo(flatNo);
		List<VehicleDetails> vehicleDetails = vehicleDetailsDao.findByflats(flats.getFlatId(),true);	
		if(vehicleDetails!=null) {	
			 //this.vehicleDetailsDao.removeVehicleDetailsByflatId(flats.getFlatId());
			 for(VehicleDetails vehicle : vehicleDetails)
			 {
				 vehicle.setVehicleStatus(false);
				 vehicleDetailsDao.save(vehicle);
			 }
			
		}
		List<Slots> slots = slotsDao.findByflatId(flats.getFlatId());
		for(Slots slot : slots)
		{
			slot.setAssigned(false);
			slot.setOccupied(false);
			slot.setFilled(false);
			slot.setFlats(null);
		slotsDao.save(slot);
		}
		long flatResidentsId=0;
		FlatOwners flatOwners=flatOwnersDao.findByownersActive(flats.getFlatId(), true);
		if(flatOwners!=null)
		{
		if(flatResidencies!=null)
		{
			
			Users userResidencies = new Users();
			userResidencies = usersDao.findByEmail(flatResidencies.getEmail());
			if(userResidencies!=null)
			{
				 Responses responses = responsesDao.findById(13);
				 //	  System.out.println("responseId"+responses.getResponsesId());
				  //	  System.out.println("resName"+responses.getResName());
				   		  
				 return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName())); 
			}
			
			userResidencies = usersDao.findByMobile(flatResidencies.getPhone());
		    if(userResidencies!=null)
			{

				 Responses responses = responsesDao.findById(14);
				 //	  System.out.println("responseId"+responses.getResponsesId());
				  //	  System.out.println("resName"+responses.getResName());
				   		  
				 return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName())); 
			}
		    FlatResidencies flatResidenciesTenants = flatResidenciesDao.findBytenantsActive(flats.getFlatId(), true);
		    if(flatResidenciesTenants==null)
		    {
		    Users userResidenciesNew = new Users();
		    userResidenciesNew.setEmail(flatResidencies.getEmail());
		    userResidenciesNew.setRoles("ROLE_TENANT");
		    userResidenciesNew.setMobile(flatResidencies.getPhone());
		    userResidenciesNew.setFirstName(flatResidencies.getFirstname());
		    userResidenciesNew.setLastName(flatResidencies.getLastName());
		    userResidenciesNew.setPassword("");
		    userResidenciesNew.setMobileVerification(true);
		    userResidenciesNew.setActive(true);
		    userDetailsService.registerNewUser(userResidenciesNew);
			
			flatResidencies.setFlats(flats);
			flatResidencies.setTenantActive(true);
			flatResidencies.setCreatedAt(new Date());
			flatResidencies=flatResidenciesDao.save(flatResidencies);
			 
			flatResidentsId=flatResidencies.getFlatresidenciesId();

		if(flatResidentsId!=0)
		{
			
			flatOwners.setFlatResidencies(flatResidencies);
			flatOwnersDao.save(flatOwners);
		}
		String message = flatResidencies.getFirstname()+" "+flatResidencies.getLastName()+" "+"your registered to the flat"+" "+flatResidencies.getFlats().getFlatNo()+" "+"with the mobile number"+" "+flatResidencies.getPhone()+" "+"Please download the app and login into it"; 
		 otpService.sendSms(flatResidencies.getPhone(), message);
		 
		Responses responses = responsesDao.findById(82);
		  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
		    }
		    else
		    {
		    	Responses responses = responsesDao.findById(25);
		 		  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
		    	
		    }
		
		    }
		}
		else
		{
			Responses responses = responsesDao.findById(36);
			  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
			
		}
		    
		return null;
	}
	
	
	 @PostMapping("/securitySignup")
     public ResponseEntity<?> securitySignup(@RequestBody BuildingSecurity buildingSecurity){
   	  Users users = new Users();
   	String email = buildingSecurity.getEmail();
 		String mobile = buildingSecurity.getMobile();
 		String firstName = buildingSecurity.getFirstName();
 		String lastName = buildingSecurity.getLastName();
 		String roles = "ROLE_SECURITY";
 		String password = "";
 		Users users1 = new Users();
		users1 = usersDao.findByEmail(email);
		if(users1!=null)
		{
			 Responses responses = responsesDao.findById(13);
			 //	  System.out.println("responseId"+responses.getResponsesId());
			  //	  System.out.println("resName"+responses.getResName());
			   		  
			 return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName())); 
		}
		
	    users1 = usersDao.findByMobile(mobile);
	    if(users1!=null)
		{

			 Responses responses = responsesDao.findById(53);
			 //	  System.out.println("responseId"+responses.getResponsesId());
			  //	  System.out.println("resName"+responses.getResName());
			   		  
			 return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName())); 
		}
	    users1 = new Users();
	    users1.setEmail(email);
		users1.setMobile(mobile);
		users1.setFirstName(firstName);
		users1.setLastName(lastName);
		users1.setRoles(roles);
		users1.setPassword(password);
		users1.setMobileVerification(true);
		users1.setActive(true);
		long userId = userDetailsService.registerNewUser(users1);	
		buildingSecurity.setSecurityActive(true);
		buildingSecurityDao.save(buildingSecurity);
	    Responses responses = responsesDao.findById(80);
  		  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
  		  
  		  
     }
	 
	 @GetMapping("/getResidentsDetailsByflatId")
		public  ResponseEntity<?> getResidentsDetailsByflatId(){
			List<Flats> flats= flatsDao.findAll();
			List<Residents> residents= new ArrayList();
			for(Flats f : flats) {
				Residents resident= new Residents();
		
			FlatOwners flatOwners = flatOwnersDao.findByownersActive(f.getFlatId(),true);
			System.out.println("after owners dao");
			  if(flatOwners!=null)
			  {
				  FlatOwnersPojo flatOwnersPojo= new FlatOwnersPojo();
				  flatOwnersPojo.setFlatNo(f.getFlatNo());
				  flatOwnersPojo.setFirstname(flatOwners.getFirstname());
				  flatOwnersPojo.setLastName(flatOwners.getLastName());
				  flatOwnersPojo.setPhone(flatOwners.getPhone());
				  flatOwnersPojo.setEmail(flatOwners.getEmail());
				  DateFormat dfCreated = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	 				if(flatOwners.getCreatedAt()!=null) {
	 					flatOwnersPojo.setCreatedAt((dfCreated.format(flatOwners.getCreatedAt())));
	 				}
				  resident.setFlatOwners(flatOwnersPojo);
			  }
			  FlatResidencies flatResidencies = flatResidenciesDao.findBytenantsActive(f.getFlatId(),true);
			  
			  System.out.println("after residencies dao");
			  if(flatResidencies!=null)
			  {
				  FlatResidenciesPojo flatresidenciesPojo= new FlatResidenciesPojo();
				  flatresidenciesPojo.setFirstname(flatResidencies.getFirstname());
				  flatresidenciesPojo.setLastName(flatResidencies.getLastName());
				  flatresidenciesPojo.setPhone(flatResidencies.getPhone());;
				  flatresidenciesPojo.setEmail(flatResidencies.getEmail());
				  flatresidenciesPojo.setFlatNo(f.getFlatNo());
				  flatresidenciesPojo.setTenantActive(flatResidencies.isTenantActive());
				  DateFormat dfCreated = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	 				if(flatResidencies.getCreatedAt()!=null) {
	 					flatresidenciesPojo.setCreatedAt((dfCreated.format(flatResidencies.getCreatedAt())));
	 				}
				  resident.setFlatResidencies(flatresidenciesPojo);
			  }
			 
			  if(flatOwners!=null)
			  {
				  List<Slots> slots = slotsDao.findByflatId(f.getFlatId());
					 if(slots!=null) {
						 List<SlotsPojo> slotsPojo = new ArrayList();
						 
						 for(Slots slot : slots) 
						 {
							
							 SlotsPojo slotPojo= new SlotsPojo();
							 slotPojo.setSlotNo(slot.getSlotNo());
							 slotPojo.setFloor(slot.getFloor());
							 slotPojo.setBlock(slot.getBlock());
							 slotPojo.setAssigned(slot.isAssigned());
							 slotPojo.setFlatNo(f.getFlatNo());
							 slotPojo.setOccupied(slot.isOccupied());
							 slotPojo.setVehicleType(slot.getVehicleType());
							 slotPojo.setBillingType(slot.getBillingType());
						 
					 }
						 resident.setSlots(slotsPojo);
					  
			  
				  }
			  }
			  if(flatOwners!=null)
			  {
			  residents.add(resident);
			  }
			  }
			
		 HashMap<String, List<Residents>> response = new HashMap<String,List<Residents>>(); 
			  response.put("residents", residents);
			  return ResponseEntity.ok(response);
		}
	 
	 @GetMapping("/getManagerDetails/{roles}")
	 public ResponseEntity<?> getManagerDetails(@PathVariable String roles){
		 List<Users> listUsers = usersDao.findByRoles(roles);
		if(roles.equalsIgnoreCase("ROLE_MANAGER"))
		{
		 HashMap<String, List<Users>> response = new HashMap<String,List<Users>>(); 
		  response.put("listUsers", listUsers);
		  return ResponseEntity.ok(response);
		}
		  else if(roles.equalsIgnoreCase("ROLE_SECURITY_MANAGER")){
			  HashMap<String, List<Users>> response = new HashMap<String,List<Users>>(); 
			  response.put("listUsers", listUsers);
			  return ResponseEntity.ok(response);
		  }
			  
		  return ResponseEntity.ok("error");
	 }
	 
	 @GetMapping("/getResidentsDetailsByType/{type}")
		public ResponseEntity<?> getResidentsDetailsByType(@PathVariable String type){
		 System.out.println(type);
		 List<Flats> flats= flatsDao.findAll();
			List<Residents> residents= new ArrayList();
			List<ActiveResidentDetailsPojo> activeResident = new ArrayList();
				for(Flats f : flats)
				{
					Residents resident= new Residents();
			
					FlatOwners flatOwners= new FlatOwners();
					if(type.equalsIgnoreCase("All"))
					{
						System.out.println("in all");
						 flatOwners = flatOwnersDao.findByownersActive(f.getFlatId(), true);	
					}
					else
					{
				 flatOwners = flatOwnersDao.findByownersActiveAndType(f.getFlatId(),true,type);
					}
				 if(flatOwners!=null)
				  {
					  FlatOwnersPojo flatOwnersPojo= new FlatOwnersPojo();
					  flatOwnersPojo.setFlatNo(f.getFlatNo());
					  flatOwnersPojo.setFirstname(flatOwners.getFirstname());
					  flatOwnersPojo.setLastName(flatOwners.getLastName());
					  flatOwnersPojo.setPhone(flatOwners.getPhone());
					  flatOwnersPojo.setEmail(flatOwners.getEmail());
					  flatOwnersPojo.setType(flatOwners.getType());
					  DateFormat dfCreated = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		 				if(flatOwners.getCreatedAt()!=null) {
		 					flatOwnersPojo.setCreatedAt((dfCreated.format(flatOwners.getCreatedAt())));
		 				}
					  resident.setFlatOwners(flatOwnersPojo);
				  }
				  FlatResidencies flatResidencies = flatResidenciesDao.findBytenantsActive(f.getFlatId(),true);
				  
				  System.out.println("after residencies dao");
				  if(flatResidencies!=null)
				  {
					  FlatResidenciesPojo flatresidenciesPojo= new FlatResidenciesPojo();
					 
					  flatresidenciesPojo.setFirstname(flatResidencies.getFirstname());
					  flatresidenciesPojo.setLastName(flatResidencies.getLastName());
					  flatresidenciesPojo.setPhone(flatResidencies.getPhone());;
					  flatresidenciesPojo.setEmail(flatResidencies.getEmail());
					  flatresidenciesPojo.setTenantActive(flatResidencies.isTenantActive());
					  DateFormat dfCreated = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		 				if(flatResidencies.getCreatedAt()!=null) {
		 					flatresidenciesPojo.setCreatedAt((dfCreated.format(flatResidencies.getCreatedAt())));
		 				}
					  resident.setFlatResidencies(flatresidenciesPojo);
					  
				  }
				  if(type.equalsIgnoreCase("activeList"))
					 {
						 flatOwners = flatOwnersDao.findByownersActive(f.getFlatId(),true);
						 ActiveResidentDetailsPojo activeResidentPojo = new ActiveResidentDetailsPojo();
						 System.out.println(f.getFlatId());
						 if(flatOwners!=null)
						 {
						 if(flatOwners.getFlatResidencies()==null) {	
							 FlatOwnersPojo flatOwnersPojo= new FlatOwnersPojo();
							  flatOwnersPojo.setFlatNo(f.getFlatNo());
							  flatOwnersPojo.setFirstname(flatOwners.getFirstname());
							  flatOwnersPojo.setLastName(flatOwners.getLastName());
							  flatOwnersPojo.setPhone(flatOwners.getPhone());
							  flatOwnersPojo.setEmail(flatOwners.getEmail());
							  DateFormat dfCreated = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				 				if(flatOwners.getCreatedAt()!=null) {
				 					flatOwnersPojo.setCreatedAt((dfCreated.format(flatOwners.getCreatedAt())));
				 				}
				 				activeResidentPojo.setResident(flatOwnersPojo);
							 
						 }
						 else {
							 
							// FlatResidenciesPojo flatresidenciesPojo= new FlatResidenciesPojo();
							 FlatOwnersPojo flatOwnersPojo= new FlatOwnersPojo();
							 flatOwnersPojo.setFirstname(flatOwners.getFlatResidencies().getFirstname());
							 flatOwnersPojo.setLastName(flatOwners.getFlatResidencies().getLastName());
							 flatOwnersPojo.setPhone(flatOwners.getFlatResidencies().getPhone());
							 flatOwnersPojo.setEmail(flatOwners.getFlatResidencies().getEmail());
							  DateFormat dfCreated = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				 				if(flatResidencies.getCreatedAt()!=null) {
				 					flatOwnersPojo.setCreatedAt((dfCreated.format(flatOwners.getFlatResidencies().getCreatedAt())));
				 				}
				 				activeResidentPojo.setResident(flatOwnersPojo);
						 }
						 List<VehicleDetails> vehicleDetails = vehicleDetailsDao.findByflats(f.getFlatId(),true);
						 
						 if(vehicleDetails!=null) 
						 {	
							 List<VehicleDetailsPojo> vehicleDetailsPojo = new ArrayList();
						 
							 for(VehicleDetails v : vehicleDetails) 
							 {
								
								 VehicleDetailsPojo vehicledetailsPojo= new VehicleDetailsPojo();
								 vehicledetailsPojo.setVehicleId(v.getVehicleId());
								 vehicledetailsPojo.setMake(v.getMake());
								 vehicledetailsPojo.setModel(v.getModel());
								 vehicledetailsPojo.setColor(v.getColor());
								 vehicledetailsPojo.setRegNo(v.getRegNo());
								 vehicledetailsPojo.setType(v.getType());
								 vehicleDetailsPojo.add(vehicledetailsPojo);
							 
						 }
							 activeResidentPojo.setVehicleDetails(vehicleDetailsPojo);
						 } 
					  if(flatOwners!=null)
					  {
						 List<Slots> slots = slotsDao.findByflatId(f.getFlatId());
						
							 List<SlotsPojo> slotsPojo = new ArrayList();
							 
							 for(Slots slot : slots) 
							 {
								 SlotsPojo slotPojo= new SlotsPojo();
								 slotPojo.setSlotNo(slot.getSlotNo());
								 System.out.println(slotPojo.getSlotNo());
								 slotPojo.setFloor(slot.getFloor());
								 slotPojo.setFlatNo(f.getFlatNo());
								 slotPojo.setAssigned(slot.isAssigned());
								 slotPojo.setVehicleType(slot.getVehicleType());
								 slotPojo.setBlock(slot.getBlock());
								 slotPojo.setOccupied(slot.isOccupied());
								 slotPojo.setBillingType(slot.getBillingType());
								 slotsPojo.add(slotPojo);
						 }
							 activeResidentPojo.setSlots(slotsPojo);
						  
					  }
						 activeResident.add(activeResidentPojo);
					 }
					 }	
				  List<VehicleDetails> vehicleDetails = vehicleDetailsDao.findByflats(f.getFlatId(),true);
				 
					 if(vehicleDetails!=null) 
					 {	
						 List<VehicleDetailsPojo> vehicleDetailsPojo = new ArrayList();
					 
						 for(VehicleDetails v : vehicleDetails) 
						 {
							
							 VehicleDetailsPojo vehicledetailsPojo= new VehicleDetailsPojo();
							 vehicledetailsPojo.setVehicleId(v.getVehicleId());
							 vehicledetailsPojo.setMake(v.getMake());
							 vehicledetailsPojo.setModel(v.getModel());
							 vehicledetailsPojo.setColor(v.getColor());
							 vehicledetailsPojo.setRegNo(v.getRegNo());
							 vehicledetailsPojo.setType(v.getType());
							 vehicleDetailsPojo.add(vehicledetailsPojo);
						 
					 }
						 resident.setVehicleDetails(vehicleDetailsPojo);
					 } 
				  if(flatOwners!=null)
				  {
					 List<Slots> slots = slotsDao.findByflatId(f.getFlatId());
					
						 List<SlotsPojo> slotsPojo = new ArrayList();
						 
						 for(Slots slot : slots) 
						 {
							 SlotsPojo slotPojo= new SlotsPojo();
							 slotPojo.setSlotNo(slot.getSlotNo());
							 System.out.println(slotPojo.getSlotNo());
							 slotPojo.setFloor(slot.getFloor());
							 slotPojo.setFlatNo(f.getFlatNo());
							 slotPojo.setAssigned(slot.isAssigned());
							 slotPojo.setVehicleType(slot.getVehicleType());
							 slotPojo.setBlock(slot.getBlock());
							 slotPojo.setOccupied(slot.isOccupied());
							 slotPojo.setBillingType(slot.getBillingType());
							 slotsPojo.add(slotPojo);
					 }
						 resident.setSlots(slotsPojo);
					  
				  }
				 
				   
				System.out.println("after owners dao");
				  if(flatOwners!=null)
				  {
				  residents.add(resident);
				  }
			}		
			
				if(type.equalsIgnoreCase("activeList"))
				{
					HashMap<String, List<ActiveResidentDetailsPojo>> response = new HashMap<String,List<ActiveResidentDetailsPojo>>();
					  response.put("residents", activeResident);
					  return ResponseEntity.ok(response);
				}
				else {
			  HashMap<String, List<Residents>> response = new HashMap<String,List<Residents>>();
			  response.put("residents", residents);
			  return ResponseEntity.ok(response);	
				}
}

	 
	 @PostMapping("/deactivatingOwnersAccount/{mobile}")
		public ResponseEntity<?> deactivatingAccount(@PathVariable String mobile ){
			Users usersDeactivate = usersDao.findByMobile(mobile);
			System.out.println(mobile);
			usersDeactivate.setActive(false);
			usersDao.save(usersDeactivate);
			FlatOwners flatOwners = flatOwnersDao.findByPhone(mobile);
			FlatOwners managementOwner = new FlatOwners();
			 managementOwner.setFlatResidencies(flatOwners.getFlatResidencies());
			flatOwners.setFlatResidencies(null);
			flatOwners.setOwnerActive(false);
			flatOwners.setUpdatedAt(new Date());
			flatOwnersDao.save(flatOwners);
			List<Slots> slots = slotsDao.findByflatId(flatOwners.getFlats().getFlatId());
			for(Slots slot: slots) {
				slot.setFlats(null);
				slot.setOccupied(false);
				slot.setAssigned(false);
                slot.setFilled(false);
				slotsDao.save(slot);
			}
			 managementOwner.setFlats(flatOwners.getFlats());
			 managementOwner.setEmail("management@gmail.com");
			 managementOwner.setPhone("9067543218");
			 managementOwner.setFirstname("management");
			 managementOwner.setLastName("management");
			 managementOwner.setOwnerActive(true);
			 managementOwner.setType("Management");
			 flatOwnersDao.save(managementOwner);
			 List<AdditionalParkingSlots> additionalParkingSlots = additionalParkingSlotsDao.findByFlats(flatOwners.getFlats());
			 for(AdditionalParkingSlots v:additionalParkingSlots) {
				 v.setSlotOccupied(false);
				 v.setFlats(null);
				 additionalParkingSlotsDao.save(v);
			 }
			 
			 Responses responses = responsesDao.findById(34);
				System.out.println("responseId" + responses.getResponsesId());
				System.out.println("resName" + responses.getResName());
				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
			
		}
		@PostMapping("/deactivatingResidenciesAccount/{mobile}")
		public ResponseEntity<?> deactivatingResidenciesAccount(@PathVariable String mobile){
			Users usersDeactivate = usersDao.findByMobile(mobile);
			usersDeactivate.setActive(false);
			usersDao.save(usersDeactivate);
			FlatResidencies flatResidencies = flatResidenciesDao.findByPhone(mobile);
			flatResidencies.setTenantActive(false);
			flatResidencies.setUpdatedAt(new Date());
			flatResidenciesDao.save(flatResidencies);
			List<Slots> slots = slotsDao.findByflatId(flatResidencies.getFlats().getFlatId());
			for(Slots slot: slots) {
				slot.setFlats(null);
				slot.setOccupied(false);
				slot.setAssigned(false);
                slot.setFilled(false);
				slotsDao.save(slot);
			}
			FlatOwners flatOwners = flatOwnersDao.findByFlatResidencies(flatResidencies);
			flatOwners.setFlatResidencies(null);
			flatOwnersDao.save(flatOwners);
			 Responses responses = responsesDao.findById(35);
				System.out.println("responseId" + responses.getResponsesId());
				System.out.println("resName" + responses.getResName());
				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		}
		
		@PostMapping("/deactivatingManager/{mobile}")
		public ResponseEntity<?> deactivatingManager(@PathVariable String mobile){
			Users usersDeactivate = usersDao.findByMobile(mobile);
			usersDeactivate.setActive(false);
			usersDao.save(usersDeactivate);
			return ResponseEntity.ok("Deactivated Sucessfully");
		}
		
		@GetMapping("/deactivatedResidentsList/{type}")
		public ResponseEntity<?> deactivatedResidentsList(@PathVariable String type){
			if(type.equalsIgnoreCase("Owner"))
			{
			List<FlatOwners> flatOwnersList = flatOwnersDao.findByownersDeactiveAndType(false, type);
			List<FlatOwnersPojo> listflatOwnersPojo= new ArrayList();
			for(FlatOwners flatOwners :flatOwnersList)
			{
			 if(flatOwners!=null)
			  {
				  FlatOwnersPojo flatOwnersPojo= new FlatOwnersPojo();
				  flatOwnersPojo.setFirstname(flatOwners.getFirstname());
				  flatOwnersPojo.setLastName(flatOwners.getLastName());
				  flatOwnersPojo.setPhone(flatOwners.getPhone());
				  flatOwnersPojo.setEmail(flatOwners.getEmail());
				  flatOwnersPojo.setType(flatOwners.getType());
				  flatOwnersPojo.setFlatNo(flatOwners.getFlats().getFlatNo());
				  DateFormat dfCreated = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	 				if(flatOwners.getCreatedAt()!=null) {
	 					flatOwnersPojo.setCreatedAt((dfCreated.format(flatOwners.getCreatedAt())));
	 				}
	 				DateFormat dfUpdated = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	 				if(flatOwners.getUpdatedAt()!=null) {
	 					flatOwnersPojo.setDeactivateddAt((dfUpdated.format(flatOwners.getUpdatedAt())));
	 				}
				
				  listflatOwnersPojo.add(flatOwnersPojo);
			  }
			}
			HashMap<String, List<FlatOwnersPojo>> response = new HashMap<String,List<FlatOwnersPojo>>();
			response.put("listflatOwnersPojo", listflatOwnersPojo);
			  return ResponseEntity.ok(response);
			}
			else if(type.equalsIgnoreCase("Tenant")) {
				List<FlatResidencies> flatResidenciesList = flatResidenciesDao.findBytenantsDeactive(false);
				List<FlatResidenciesPojo> listFlatResidenciesPojo= new ArrayList();
				for(FlatResidencies flatResidencies :flatResidenciesList)
				{
					if(flatResidencies!=null)
					  {
						  FlatResidenciesPojo flatresidenciesPojo= new FlatResidenciesPojo();
						  flatresidenciesPojo.setFirstname(flatResidencies.getFirstname());
						  flatresidenciesPojo.setLastName(flatResidencies.getLastName());
						  flatresidenciesPojo.setPhone(flatResidencies.getPhone());;
						  flatresidenciesPojo.setEmail(flatResidencies.getEmail());
						  flatresidenciesPojo.setTenantActive(flatResidencies.isTenantActive());
						  DateFormat dfCreated = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
			 				if(flatResidencies.getCreatedAt()!=null) {
			 					flatresidenciesPojo.setCreatedAt((dfCreated.format(flatResidencies.getCreatedAt())));
			 				}
			 				DateFormat dfUpdated = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
			 				if(flatResidencies.getUpdatedAt()!=null) {
			 					flatresidenciesPojo.setDeactivateddAt((dfUpdated.format(flatResidencies.getUpdatedAt())));
			 				}
			 				flatresidenciesPojo.setFlatNo(flatResidencies.getFlats().getFlatNo());
						  listFlatResidenciesPojo.add(flatresidenciesPojo);
					  }
				}
				
				HashMap<String, List<FlatResidenciesPojo>> response = new HashMap<String,List<FlatResidenciesPojo>>();
				response.put("listflatOwnersPojo", listFlatResidenciesPojo);
				  return ResponseEntity.ok(response);
			}
			
			return null;
		}
}
