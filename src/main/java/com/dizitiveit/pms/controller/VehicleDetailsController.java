package com.dizitiveit.pms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.Dao.AdditionalParkingSlotsDao;
import com.dizitiveit.pms.Dao.FlatDetailsDao;
import com.dizitiveit.pms.Dao.FlatOwnersDao;
import com.dizitiveit.pms.Dao.FlatResidenciesDao;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.Dao.SlotsDao;
import com.dizitiveit.pms.Dao.UsersDao;
import com.dizitiveit.pms.Dao.VehicleDetailsDao;
import com.dizitiveit.pms.model.AdditionalParkingSlots;
import com.dizitiveit.pms.model.FlatDetails;
import com.dizitiveit.pms.model.FlatOwners;
import com.dizitiveit.pms.model.FlatResidencies;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.Slots;
import com.dizitiveit.pms.model.Users;
import com.dizitiveit.pms.model.VehicleDetails;
import com.dizitiveit.pms.pojo.VehicleDetailsPojo;
import com.dizitiveit.pms.service.MyUserDetailsService;

import lombok.experimental.PackagePrivate;

@RestController
@RequestMapping("/vehicle")
public class VehicleDetailsController {
	
	@Autowired
	private MyUserDetailsService userService;
	
	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private FlatsDao flatsDao;
	
	@Autowired
	private VehicleDetailsDao vehicleDetailsDao;
	
	@Autowired
	private ResponsesDao responsesDao;
	
	@Autowired
	private FlatOwnersDao flatOwnersDao;
	
	@Autowired
	private FlatResidenciesDao flatResidenciesDao;
	
	@Autowired
	private FlatDetailsDao flatDetailsDao;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private AdditionalParkingSlotsDao additionalParkingSlotsDao;
	
    @Autowired
    private SlotsDao slotsDao;
	
	@PostMapping(value="/saveDetails/{flatNo}")
	public ResponseEntity<?> saveDetails(@PathVariable String flatNo,@RequestBody VehicleDetails vehicles){
		  Flats flats = flatsDao.findByflatNo(flatNo);
		VehicleDetails vehicleDetails = new VehicleDetails();
			vehicleDetails=vehicleDetailsDao.findByRegno(vehicles.getRegNo());
			if(vehicleDetails==null)
			{

		System.out.println(flatNo);
		FlatOwners flatOwners = flatOwnersDao.findByFlats(flats);
		if(flatOwners.getFlatResidencies()==null)
		{
			vehicles.setFlatOwners(flatOwners);
			System.out.println(flatOwners.toString());
			  vehicles.setCreatedAt(new Date());
			   vehicles.setFlats(flats);
			   vehicles.setVehicleStatus(true);
			vehicleDetailsDao.save(vehicles);
		}
		else {
			vehicles.setFlatResidencies(flatOwners.getFlatResidencies());
			  vehicles.setCreatedAt(new Date());
			   vehicles.setFlats(flats);
			   vehicles.setVehicleStatus(true);
			vehicleDetailsDao.save(vehicles);
		}
		 Responses responses = responsesDao.findById(20);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
			
		}
			else {
			 Responses responses = responsesDao.findById(33);
				System.out.println("responseId" + responses.getResponsesId());
				System.out.println("resName" + responses.getResName());
				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
			}
		}
		
		@PostMapping("/vehicleRegistrationForFlat/{flatNo}")
		public ResponseEntity<?> vehicleRegistrationForFlat(@PathVariable String flatNo,@RequestBody VehicleDetails vehicles){
			  Flats flats = flatsDao.findByflatNo(flatNo);
				VehicleDetails vehicleDetails = new VehicleDetails();
					vehicleDetails=vehicleDetailsDao.findByRegno(vehicles.getRegNo());
					if(vehicleDetails==null)
					{
						System.out.println(flatNo);
						FlatOwners flatOwners = flatOwnersDao.findByFlats(flats);
						if(flatOwners.getFlatResidencies()==null)
						{
							vehicles.setFlatOwners(flatOwners);
							System.out.println(flatOwners.toString());
							  vehicles.setCreatedAt(new Date());
							   vehicles.setFlats(flats);
							   vehicles.setVehicleStatus(true);
							vehicleDetailsDao.save(vehicles);
						}
						else {
							vehicles.setFlatResidencies(flatOwners.getFlatResidencies());
							  vehicles.setCreatedAt(new Date());
							   vehicles.setFlats(flats);
							   vehicles.setVehicleStatus(true);
							vehicleDetailsDao.save(vehicles);
						}
						 Responses responses = responsesDao.findById(20);
							System.out.println("responseId" + responses.getResponsesId());
							System.out.println("resName" + responses.getResName());
							return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
							
					}
							else {
							 Responses responses = responsesDao.findById(33);
								System.out.println("responseId" + responses.getResponsesId());
								System.out.println("resName" + responses.getResName());
								return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
							}
		}
		

	 @GetMapping(value="/retrieveVehicles")
	   public ResponseEntity<?> retrieveVehicles() {
		 Users users = userDetailsService.getAuthUser();
		 List<VehicleDetailsPojo> vehicleDetailsPojoList= new ArrayList();
		   try {
			   if(users.getRoles().equalsIgnoreCase("ROLE_OWNER")) 
			   {
				   FlatOwners flatOwners = flatOwnersDao.findByownersPhone(users.getMobile(),true);
				   if(flatOwners.getFlatResidencies()==null)
				   {
					   List<VehicleDetails> vehicleDetails = vehicleDetailsDao.findByflatsAndOwners(flatOwners.getFlats().getFlatId(),flatOwners.getFlatownersId());
				    	
						   for(VehicleDetails v : vehicleDetails) {
							   VehicleDetailsPojo vehicleDetailsPojo = new VehicleDetailsPojo();
							   vehicleDetailsPojo.setVehicleId(v.getVehicleId());
							   vehicleDetailsPojo.setMake(v.getMake());
							   vehicleDetailsPojo.setModel(v.getModel());
							   vehicleDetailsPojo.setFlatNo(v.getFlats().getFlatNo());
							   vehicleDetailsPojo.setColor(v.getColor());
							   vehicleDetailsPojo.setRegNo(v.getRegNo());
							   vehicleDetailsPojo.setType(v.getType());
							   vehicleDetailsPojo.setVehicleStatus(v.isVehicleStatus());
							   if(v.getFlatOwners()!=null)
							   {
								   
								   vehicleDetailsPojo.setName(v.getFlatOwners().getFirstname());
								   vehicleDetailsPojo.setPhone(v.getFlatOwners().getPhone());
							   }
							   else if(v.getFlatResidencies()!=null)
							   {
								   vehicleDetailsPojo.setName(v.getFlatResidencies().getFirstname());
								   vehicleDetailsPojo.setPhone(v.getFlatResidencies().getPhone());
							   }
								// vehicleDetailsPojo.add(vehicleDetails);
							   vehicleDetailsPojoList.add(vehicleDetailsPojo);
						   
						   }
						   HashMap<String, List<VehicleDetailsPojo>> response = new HashMap<String,List<VehicleDetailsPojo>>();
				              response.put("OwnerVehicleDetails",vehicleDetailsPojoList);
						 return ResponseEntity.ok(response);
				   }
				   else
				   {
				  
					   HashMap<String, List<VehicleDetailsPojo>> response = new HashMap<String,List<VehicleDetailsPojo>>();
			              response.put("EmptyVehicleDetails",vehicleDetailsPojoList);
					 return ResponseEntity.ok(response);
				   }
			   }
			   else if(users.getRoles().equalsIgnoreCase("ROLE_TENANT"))
			   {
				  FlatResidencies flatResidencies=flatResidenciesDao.findByPhone(users.getMobile());
				  FlatOwners flatOwners= flatOwnersDao.findByFlatResidencies(flatResidencies);
				  List<VehicleDetails> vehicleDetails = vehicleDetailsDao.findByflatsAndTenant(flatOwners.getFlats().getFlatId(),flatResidencies.getFlatresidenciesId());
			    	
					   for(VehicleDetails v : vehicleDetails) {
						   VehicleDetailsPojo vehicleDetailsPojo = new VehicleDetailsPojo();
						   vehicleDetailsPojo.setVehicleId(v.getVehicleId());
						   vehicleDetailsPojo.setMake(v.getMake());
						   vehicleDetailsPojo.setModel(v.getModel());
						   vehicleDetailsPojo.setFlatNo(v.getFlats().getFlatNo());
						   vehicleDetailsPojo.setColor(v.getColor());
						   vehicleDetailsPojo.setRegNo(v.getRegNo());
						   vehicleDetailsPojo.setType(v.getType());
						   vehicleDetailsPojo.setVehicleStatus(v.isVehicleStatus());
							// vehicleDetailsPojo.add(vehicleDetails);
						   vehicleDetailsPojoList.add(vehicleDetailsPojo);
					   
					   }
					   HashMap<String, List<VehicleDetailsPojo>> response = new HashMap<String,List<VehicleDetailsPojo>>();
			              response.put("TenantVehicleDetails",vehicleDetailsPojoList);
					 return ResponseEntity.ok(response);
			   }
			   HashMap<String, List<VehicleDetailsPojo>> response = new HashMap<String,List<VehicleDetailsPojo>>();
	              response.put("listVehicleDetails",vehicleDetailsPojoList);
			 return ResponseEntity.ok(response);
		   }
		    catch (Exception E) {
			   ResponseEntity.badRequest();
			   return ResponseEntity.ok(E);				
			}
			
		}
	 
	 @GetMapping(value="/getDeactivateList/{flatNo}")
	 public ResponseEntity<?> getDeactivateList(@PathVariable String flatNo){
		 Flats flats = flatsDao.findByflatNo(flatNo); 
		 List<VehicleDetailsPojo> vehicleDetailsPojoList= new ArrayList();
		 List<VehicleDetails> vehicleDetails = vehicleDetailsDao.findByflatsAndVehicleStatus(flats.getFlatId(),false);
		 for(VehicleDetails v : vehicleDetails) {
			   VehicleDetailsPojo vehicleDetailsPojo = new VehicleDetailsPojo();
			   vehicleDetailsPojo.setVehicleId(v.getVehicleId());
			   vehicleDetailsPojo.setMake(v.getMake());
			   vehicleDetailsPojo.setModel(v.getModel());
			   vehicleDetailsPojo.setFlatNo(v.getFlats().getFlatNo());
			   vehicleDetailsPojo.setColor(v.getColor());
			   vehicleDetailsPojo.setRegNo(v.getRegNo());
			   vehicleDetailsPojo.setType(v.getType());
				// vehicleDetailsPojo.add(vehicleDetails);
			   vehicleDetailsPojoList.add(vehicleDetailsPojo);
		   }
		 HashMap<String, List<VehicleDetailsPojo>> response = new HashMap<String,List<VehicleDetailsPojo>>();
         response.put("OwnerVehicleDetails",vehicleDetailsPojoList);
	 return ResponseEntity.ok(response);
	 }
	 
	 @PostMapping(value="/updateVehicle/{vehicleId}")
	 public ResponseEntity<?> updateVehicle(@PathVariable long vehicleId,@RequestBody VehicleDetails vehicles){
		// Flats flats = flatsDao.findByflatNo(flatNo); 
		 VehicleDetails vehiclesUpdate = vehicleDetailsDao.findByvehicleId(vehicleId);
		    if(vehiclesUpdate!=null)
		    {  	
		     vehiclesUpdate.setUpdatedAt(new Date()); 	
		     vehiclesUpdate.setFlats(vehicles.getFlats());
			 vehiclesUpdate.setColor(vehicles.getColor());
			 vehiclesUpdate.setMake(vehicles.getMake());
			 vehiclesUpdate.setModel(vehicles.getModel());
			 vehiclesUpdate.setRegNo(vehicles.getRegNo());
			 vehiclesUpdate.setVehicleStatus(vehicles.isVehicleStatus());
			 vehiclesUpdate.setType(vehicles.getType());
			 vehicleDetailsDao.save(vehiclesUpdate);
		    }
		    Responses responses = responsesDao.findById(19);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
	 }
	 
	 @PostMapping("activatingVehicle/{vehicleId}")
	 public ResponseEntity<?> activatingVehicle(@PathVariable long vehicleId)
	 {
		 VehicleDetails vehicleActivate = vehicleDetailsDao.findByvehicleId(vehicleId);
		 if(vehicleActivate!=null) {
			 vehicleActivate.setVehicleStatus(true);
			 vehicleDetailsDao.save(vehicleActivate);
		 }
		 Responses responses = responsesDao.findById(66);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
	 }
	 
	 @PostMapping("deactivateSingleVehicle/{vehicleId}")
	 public ResponseEntity<?> deactivateSingleVehicle(@PathVariable long vehicleId){
		 VehicleDetails vehicleDeactivate = vehicleDetailsDao.findByvehicleId(vehicleId);
		 if(vehicleDeactivate!=null) {
			 List<Slots> slots = slotsDao.getByvehicleType(vehicleDeactivate.getFlats().getFlatId(),vehicleDeactivate.getType(),true);
			 for(Slots slot:slots) {
			 if(slot.isOccupied()==true) {
				 slot.setOccupied(false);
				 slotsDao.save(slot);
			 }
			 else {
				 Responses responses = responsesDao.findById(65);
					System.out.println("responseId" + responses.getResponsesId());
					System.out.println("resName" + responses.getResName());
					return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
			 }
			 }
			 vehicleDeactivate.setVehicleStatus(false);
			 vehicleDetailsDao.save(vehicleDeactivate);
		 }
		 Responses responses = responsesDao.findById(64);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
	 }
	 
	 @PostMapping("deactivatingVehicleByFlat/{flatNo}")
	 public ResponseEntity<?> deactivatingVehicleByFlat(@PathVariable String flatNo){
		 Flats flats = flatsDao.findByflatNo(flatNo); 
		    //System.out.println("vehicle id is"+vehicleId);  
		 List<VehicleDetails> vehicleDetails = vehicleDetailsDao.findByflatsAndVehicleStatus(flats.getFlatId(),true);
		 if(vehicleDetails!=null) {
			 for(VehicleDetails vehicle: vehicleDetails) {
				 vehicle.setVehicleStatus(false);
				 vehicleDetailsDao.save(vehicle);
				 vehicleDetails.add(vehicle);
			 }
			 List<Slots> slots = slotsDao.findByflatId(flats.getFlatId());
			    for(Slots slot:slots) {
			    	slot.setOccupied(false);
			    	slot.setAssigned(false);
			    	slot.setFlats(null);
			    	slotsDao.save(slot);
			    	slots.add(slot);
			    }
			    Responses responses = responsesDao.findById(64);
				System.out.println("responseId" + responses.getResponsesId());
				System.out.println("resName" + responses.getResName());
				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		 }
		 else {
			 Responses responses = responsesDao.findById(63);
				System.out.println("responseId" + responses.getResponsesId());
				System.out.println("resName" + responses.getResName());
				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		 }
		    
	 }
	 @DeleteMapping(value="/deleteVehicle/{vehicleId}")
	 public ResponseEntity<?> deleteVehicle(@PathVariable long vehicleId){
		 try {
			    System.out.println("vehicle id is"+vehicleId);
			    VehicleDetails vehicleDetails = vehicleDetailsDao.findByvehicleId(vehicleId);
			    this.vehicleDetailsDao.deleteById(vehicleId);
		 } 
				 
			 catch (Exception E) {
					E.printStackTrace();
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return ResponseEntity.status(500).body(E.getMessage());
				}
			 
		 Responses responses = responsesDao.findById(44);
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		   }
	 
	 @DeleteMapping(value="/deleteVehicleByFlat/{flatNo}")
	 public ResponseEntity<?> deleteVehicle(@PathVariable String flatNo)
	 {
		 try {
			 Flats flats = flatsDao.findByflatNo(flatNo); 
			    //System.out.println("vehicle id is"+vehicleId);  
			  List<Slots> slots = slotsDao.findByflatId(flats.getFlatId());
			    for(Slots slot:slots) {
			    	slot.setOccupied(false);
			    	slot.setFlats(null);
			    	slot.setAssigned(false);
			    	slotsDao.save(slot);
			    	slots.add(slot);
			    }		
				 this.vehicleDetailsDao.removeVehicleDetailsByflatId(flats.getFlatId());
			 }catch (Exception E) {
					E.printStackTrace();
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return ResponseEntity.status(500).body(E.getMessage());
				}
			 
		 Responses responses = responsesDao.findById(44);
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		   }
	 
	 
		 
	 }
	

