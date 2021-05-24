package com.dizitiveit.pms.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.Dao.BuildingSecurityDao;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.ReLoginDetailsDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.Dao.SecurityLoginDetailsDao;
import com.dizitiveit.pms.Dao.SecurityShiftsDao;
import com.dizitiveit.pms.Dao.SlotsDao;
import com.dizitiveit.pms.Dao.VehicleDetailsDao;
import com.dizitiveit.pms.model.BuildingSecurity;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.ReLoginDetails;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.SecurityShifts;
import com.dizitiveit.pms.model.Slots;
import com.dizitiveit.pms.model.Users;
import com.dizitiveit.pms.model.VehicleDetails;
import com.dizitiveit.pms.pojo.BuildingSecurityPojo;
import com.dizitiveit.pms.pojo.FlatsVehicleDetailsPojo;
import com.dizitiveit.pms.pojo.SecurityShiftsPojo;
import com.dizitiveit.pms.pojo.SlotsPojo;
import com.dizitiveit.pms.pojo.VehicleDetailsPojo;
import com.dizitiveit.pms.service.MyUserDetailsService;

@RestController
@RequestMapping("/security")
public class BuildingSecurityController {
	
	@Autowired
	private BuildingSecurityDao buildingSecurityDao;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private ReLoginDetailsDao reLoginDetailsDao;
	
	@Autowired
	private ResponsesDao responsesDao;
	
	@Autowired
	private SecurityShiftsDao securityShiftsDao; 
	
	@Autowired
	private SecurityLoginDetailsDao securityLoginDetailsDao; 
	
	@Autowired
	private FlatsDao flatsDao;
	
	@Autowired
	private VehicleDetailsDao vehicleDetailsDao;
	
	@Autowired
	private SlotsDao slotsDao;

	@GetMapping("/getAllSecurityDetails")
	public ResponseEntity<?> getAllSecurityDetails(){
		List<BuildingSecurity> buildingSecurity = buildingSecurityDao.getAllSecurities(true);
		 HashMap<String, List<BuildingSecurity>> response = new HashMap<String,List<BuildingSecurity>>();
         response.put("buildingSecurity",buildingSecurity);
	 return ResponseEntity.ok(response);
	}
	
	@GetMapping("/getSecurityDetails")
	public ResponseEntity<?> getSecurityDetails(){
		Users users = userDetailsService.getAuthUser();
		BuildingSecurity buildingSecurity = buildingSecurityDao.getByMobile(users.getMobile(),true);
		ReLoginDetails reLoginDetails = new ReLoginDetails();
		reLoginDetails.setReLoginTime(new Date());
		reLoginDetails.setBuildingSecurity(buildingSecurity);
		reLoginDetailsDao.save(reLoginDetails);
		return ResponseEntity.status(200).body(buildingSecurity);
		
	}
	
	@PostMapping("/deactiveSecurityGaurd/{securityId}")
	public ResponseEntity<?> deactiveSecurityGaurd(@PathVariable long securityId)
	{
		BuildingSecurity buildingSecurity = buildingSecurityDao.findBySecurityId(securityId);
		buildingSecurity.setSecurityActive(false);
		buildingSecurityDao.save(buildingSecurity);
		 Responses responses = responsesDao.findById(77);
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
	}
	
	@PostMapping("/activeSecurityGaurd/{securityId}")
	public ResponseEntity<?> activeSecurityGaurd(@PathVariable long securityId)
	{
		BuildingSecurity buildingSecurity = buildingSecurityDao.findBySecurityId(securityId);
		buildingSecurity.setSecurityActive(true);
		buildingSecurityDao.save(buildingSecurity);
		 Responses responses = responsesDao.findById(77);
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
	}
	
	@DeleteMapping("/deleteSecurityGaurd/{securityId}")
	public ResponseEntity<?> deleteSecurityGaurd(@PathVariable long securityId){
		this.securityLoginDetailsDao.removeSecurityLoginDetailsBysecurityId(securityId);
		this.securityShiftsDao.removeSecurityShitsBysecurityId(securityId);
		this.reLoginDetailsDao.removeReLoginDetailsBysecurityId(securityId);
		BuildingSecurity buildingSecurity = buildingSecurityDao.deleteById(securityId);
		 Responses responses = responsesDao.findById(54);
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		   }
		
	@GetMapping("/getSecurityShiftDetails/{securityId}")
	public ResponseEntity<?> getSecurityShiftDetails(@PathVariable long securityId){
		Users users = userDetailsService.getAuthUser();
		BuildingSecurityPojo buildingSecurityPojo = new BuildingSecurityPojo();
		BuildingSecurity buildingSecurity = buildingSecurityDao.findBySecurityId(securityId);
		buildingSecurityPojo.setBuildingSecurity(buildingSecurity);
		List<SecurityShifts> securityShifts=securityShiftsDao.findByBuildingSecurity(buildingSecurity);
		 Responses responses = responsesDao.findById(54);
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));

	}
	
	@GetMapping("/securityShifts/{securityId}")
	public ResponseEntity<?> securityShifts(@PathVariable long securityId){
		List<SecurityShifts> securityShiftsList =  securityShiftsDao.findByShiftSlotBySecurityId(securityId, true);
		List<SecurityShiftsPojo> listsecurityShiftPojo= new ArrayList();
		for(SecurityShifts securityShift : securityShiftsList)
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
		 HashMap<String,List<SecurityShiftsPojo>> response = new HashMap<String,List<SecurityShiftsPojo>>();
			response.put("listsecurityShiftPojo", listsecurityShiftPojo);
			return ResponseEntity.ok(response);
	}
	
	@GetMapping("/securityShiftsHistory/{securityId}")
	public ResponseEntity<?> securityShiftsHistory(@PathVariable long securityId){
		List<SecurityShifts> securityShiftsList =  securityShiftsDao.findByShiftSlotBySecurityId(securityId, false);
		List<SecurityShiftsPojo> listsecurityShiftPojo= new ArrayList();
		for(SecurityShifts securityShift : securityShiftsList)
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
		 HashMap<String,List<SecurityShiftsPojo>> response = new HashMap<String,List<SecurityShiftsPojo>>();
			response.put("listsecurityShiftPojo", listsecurityShiftPojo);
	return ResponseEntity.ok(response);
	}
	
	
	  @GetMapping("/getVehiclesOfAllFlats/{regNo}") 
	  public ResponseEntity<?> getVehiclesOfAllFlats(@PathVariable String regNo) {
	  List<Flats> flats = flatsDao.findAll(); 
	  //List<FlatsVehicleDetailsPojo> flatVehicleDetails = new ArrayList();
	  for(Flats flat : flats) {
		 // FlatsVehicleDetailsPojo flatVehicle = new FlatsVehicleDetailsPojo();

		  VehicleDetails vehicleDetails = vehicleDetailsDao.findByregNo(regNo);
		  VehicleDetailsPojo  vehicleDetailsPojo = new VehicleDetailsPojo();
		  vehicleDetailsPojo.setVehicleId(vehicleDetails.getVehicleId());
		  vehicleDetailsPojo.setMake(vehicleDetails.getMake());
		  vehicleDetailsPojo.setModel(vehicleDetails.getModel());
		  vehicleDetailsPojo.setFlatNo(vehicleDetails.getFlats().getFlatNo());
		  vehicleDetailsPojo.setColor(vehicleDetails.getColor());
		  vehicleDetailsPojo.setRegNo(vehicleDetails.getRegNo());
		  vehicleDetailsPojo.setType(vehicleDetails.getType());
		  vehicleDetailsPojo.setVehicleStatus(vehicleDetails.isVehicleStatus());
		  if(vehicleDetails.getFlatOwners()!=null) {
		  
		  vehicleDetailsPojo.setName(vehicleDetails.getFlatOwners().getFirstname());
		  vehicleDetailsPojo.setPhone(vehicleDetails.getFlatOwners().getPhone()); } else
		  if(vehicleDetails.getFlatResidencies()!=null) {
		  vehicleDetailsPojo.setName(vehicleDetails.getFlatResidencies().getFirstname());
		  vehicleDetailsPojo.setPhone(vehicleDetails.getFlatResidencies().getPhone());
		  } 
		 
		  
		 // flatVehicle.setVehicleDetailsPojo(vehicleDetailsPojo);	  
		 
	  HashMap<String, VehicleDetailsPojo> response = new HashMap<String,VehicleDetailsPojo>();
      response.put("vehicleDetailsPojo",vehicleDetailsPojo);
         return ResponseEntity.ok(response);
	  }

         return ResponseEntity.ok("error");
	} 
	  
	 
	 @GetMapping("/getFlatSlots/{flatNo}")
	 public ResponseEntity<?> getFlatSlots(@PathVariable String flatNo){
		 Flats flats = flatsDao.findByflatNo(flatNo);		 
		 List<Slots> slots = slotsDao.findByflatId(flats.getFlatId());
		 HashMap<String, List<Slots>> response = new HashMap<String,List<Slots>>();
         response.put("slotsList",slots);
		 return ResponseEntity.ok(response);
	 }
	 
	 @GetMapping("/getAllRegNo")
	 public ResponseEntity<?> getAllRegNo(){
		 List<String> vehicles = vehicleDetailsDao.findAllVehicles(true);	
		 HashMap<String, List<String>> response = new HashMap<String,List<String>>();
         response.put("listRegnos",vehicles);
		 return ResponseEntity.ok(response);
	 }
}	
	
