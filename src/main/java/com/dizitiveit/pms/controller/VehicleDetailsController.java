package com.dizitiveit.pms.controller;

import java.util.ArrayList;
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

import com.dizitiveit.pms.Dao.FlatDetailsDao;
import com.dizitiveit.pms.Dao.FlatOwnersDao;
import com.dizitiveit.pms.Dao.FlatResidenciesDao;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.Dao.UsersDao;
import com.dizitiveit.pms.Dao.VehicleDetailsDao;
import com.dizitiveit.pms.model.FlatDetails;
import com.dizitiveit.pms.model.FlatOwners;
import com.dizitiveit.pms.model.FlatResidencies;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.Users;
import com.dizitiveit.pms.model.VehicleDetails;
import com.dizitiveit.pms.pojo.VehicleDetailsPojo;
import com.dizitiveit.pms.service.MyUserDetailsService;

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
	private FlatDetailsDao flatDetailsDao;
	
	@Autowired
	private FlatResidenciesDao flatResidenciesDao;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@PostMapping(value="/saveDetails/{flatNo}/{slot}")
	public ResponseEntity<?> saveDetails(@PathVariable int flatNo,@RequestBody VehicleDetails vehicles,@PathVariable String slot){
		  Flats flats = flatsDao.findByflatNo(flatNo);
		VehicleDetails vehicleDetails = new VehicleDetails();

			vehicleDetails=vehicleDetailsDao.findByRegno(vehicles.getRegNo());
			if(vehicleDetails==null)
			{
		//Flats flats = new Flats();
		//Flats flats=flatsDao.findByflatNo(flatNo);
		System.out.println(flatNo);
		FlatDetails flatDetails = flatDetailsDao.findByFlats(flats);
		if(flatDetails!=null)
		{
		if(vehicles.getType().equalsIgnoreCase("2-wheeler"))
		{
			if(slot.contains("b1")) {
				flatDetails.setB1Occupied(true);
			     
			}
			else if(slot.contains("b2")) {
				flatDetails.setB2Occupied(true);
			}
			
			flatDetailsDao.save(flatDetails);
			vehicles.setFlatDetails(flatDetails);
				   vehicles.setFlats(flats);
				vehicleDetailsDao.save(vehicles);
		}
		 if(vehicles.getType().equalsIgnoreCase("4-wheeler"))
		 {
			 if(slot.contains("c1")) {
					flatDetails.setC1Occupied(true);
				     
				}
				else if(slot.contains("c2")) {
					flatDetails.setC2Occupied(true);
				}
		flatDetailsDao.save(flatDetails);
		vehicles.setFlatDetails(flatDetails);
			   vehicles.setFlats(flats);
			vehicleDetailsDao.save(vehicles);
		}
			 Responses responses = responsesDao.findById(20);
				System.out.println("responseId" + responses.getResponsesId());
				System.out.println("resName" + responses.getResName());
				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		}
		else
		{
			 Responses responses = responsesDao.findById(32);
				System.out.println("responseId" + responses.getResponsesId());
				System.out.println("resName" + responses.getResName());
				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
			
		}
			}
		else
		{
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
					   List<VehicleDetails> vehicleDetails = vehicleDetailsDao.findByflats(flatOwners.getFlats().getFlatId());
				    	
						   for(VehicleDetails v : vehicleDetails) {
							   VehicleDetailsPojo vehicleDetailsPojo = new VehicleDetailsPojo();
							   vehicleDetailsPojo.setVehicleId(v.getVehicleId());
							   vehicleDetailsPojo.setMake(v.getMake());
							   vehicleDetailsPojo.setModel(v.getModel());
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
				  List<VehicleDetails> vehicleDetails = vehicleDetailsDao.findByflats(flatOwners.getFlats().getFlatId());
			    	
					   for(VehicleDetails v : vehicleDetails) {
						   VehicleDetailsPojo vehicleDetailsPojo = new VehicleDetailsPojo();
						   vehicleDetailsPojo.setVehicleId(v.getVehicleId());
						   vehicleDetailsPojo.setMake(v.getMake());
						   vehicleDetailsPojo.setModel(v.getModel());
						   vehicleDetailsPojo.setColor(v.getColor());
						   vehicleDetailsPojo.setRegNo(v.getRegNo());
						   vehicleDetailsPojo.setType(v.getType());
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
	 
	 @GetMapping(value="/retrieveVehiclesbystatus/{status}")
	   public ResponseEntity<?> retrieveVehiclesbystatus (@PathVariable String status) {
		   try {
			 List<VehicleDetails>listVehicleDetails=vehicleDetailsDao.findBystatus(status);
			 HashMap<String, List<VehicleDetails>> response = new HashMap<String,List<VehicleDetails>>();
	              response.put("listVehicleDetails",listVehicleDetails);
			 return ResponseEntity.ok(response);
		   }catch (Exception E) {
			   ResponseEntity.badRequest();
			   return ResponseEntity.ok(E);
			}
		}
	 
	 @PostMapping(value="/updateVehicle/{flatNo}/{slot}")
	 public ResponseEntity<?> updateVehicle(@PathVariable int flatNo,@RequestBody VehicleDetails vehicles,@PathVariable String slot){
		 Flats flats = flatsDao.findByflatNo(flatNo); 
		 VehicleDetails vehiclesUpdate = vehicleDetailsDao.findByFlats(flats);
		 FlatDetails flatDetails = flatDetailsDao.findByFlats(flats);
		 flatDetails.setB1Occupied(false);
		 flatDetails.setB2Occupied(false);
		 flatDetails.setC1Occupied(false);
		 flatDetails.setC2Occupied(false);
		 System.out.println(flatDetails);
		 flatDetailsDao.save(flatDetails);
		 if(vehiclesUpdate.getType().equalsIgnoreCase("2-wheeler")) {
			 if(slot.contains("b1")) {
					flatDetails.setB1Occupied(true);
				     
				}
				else if(slot.contains("b2")) {
					flatDetails.setB2Occupied(true);
				}
			 flatDetailsDao.save(flatDetails);
		 vehiclesUpdate.setColor(vehicles.getColor());
		 vehiclesUpdate.setMake(vehicles.getMake());
		 vehiclesUpdate.setModel(vehicles.getModel());
		 vehiclesUpdate.setRegNo(vehicles.getRegNo());
		 vehiclesUpdate.setStatus(vehicles.getStatus());
		 vehiclesUpdate.setType(vehicles.getType());
		 vehicleDetailsDao.save(vehiclesUpdate);
		 }
		  if(vehiclesUpdate.getType().equalsIgnoreCase("4-wheeler")) {
			  if(slot.contains("c1")) {
					flatDetails.setC1Occupied(true);
				     
				}
				else if(slot.contains("c2")) {
					flatDetails.setC2Occupied(true);
				}
			 flatDetailsDao.save(flatDetails);
			 vehiclesUpdate.setColor(vehicles.getColor());
			 vehiclesUpdate.setMake(vehicles.getMake());
			 vehiclesUpdate.setModel(vehicles.getModel());
			 vehiclesUpdate.setRegNo(vehicles.getRegNo());
			 vehiclesUpdate.setStatus(vehicles.getStatus());
			 vehiclesUpdate.setType(vehicles.getType());
			 vehicleDetailsDao.save(vehiclesUpdate);
		 }
		 Responses responses = responsesDao.findById(19);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
	 }
	 
	 @DeleteMapping(value="/deleteVehicle/{vehicleId}")
	 public ResponseEntity<?> deleteVehicle(@PathVariable long vehicleId){
		 try {
			    System.out.println("vehicle id is"+vehicleId); 
			    VehicleDetails vehicleDetails = vehicleDetailsDao.findByvehicleId(vehicleId);
			    FlatDetails flatDetails = flatDetailsDao.findByFlats(vehicleDetails.getFlats());
			    if(vehicleDetails.getType().equalsIgnoreCase("2-wheeler")) {
			    	 if(flatDetails.isB1Occupied()==true) {
							flatDetails.setB1Occupied(false);
						     
						}
						else if(flatDetails.isB2Occupied()==true) {
							flatDetails.setB2Occupied(false);
						}
					 flatDetailsDao.save(flatDetails);
			    this.vehicleDetailsDao.deleteById(vehicleId);
			    }
			    if(vehicleDetails.getType().equalsIgnoreCase("4-wheeler")) {
			    	 if(flatDetails.isC1Occupied()==true) {
							flatDetails.setC1Occupied(false);
						     
						}
						else if(flatDetails.isC2Occupied()==true) {
							flatDetails.setC2Occupied(false);
						}
					 flatDetailsDao.save(flatDetails);
			    this.vehicleDetailsDao.deleteById(vehicleId);
			    }
				 
			 }catch (Exception E) {
					E.printStackTrace();
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return ResponseEntity.status(500).body(E.getMessage());
				}
			 
		 Responses responses = responsesDao.findById(44);
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		   }
	 
	 @DeleteMapping(value="/deleteVehicleByFlat/{flatNo}")
	 public ResponseEntity<?> deleteVehicle(@PathVariable int flatNo){
		 try {
			 Flats flats = flatsDao.findByflatNo(flatNo); 
			    //System.out.println("vehicle id is"+vehicleId);  
			    FlatDetails flatDetails = flatDetailsDao.findByFlats(flats);
			    
				 flatDetails.setB1Occupied(false);
				 flatDetails.setB2Occupied(false);
				 flatDetails.setC1Occupied(false);
				 flatDetails.setC2Occupied(false);
				 System.out.println(flatDetails);
				 flatDetailsDao.save(flatDetails);		
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
	

