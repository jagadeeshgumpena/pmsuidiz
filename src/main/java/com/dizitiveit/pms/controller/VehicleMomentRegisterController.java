package com.dizitiveit.pms.controller;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.Dao.SlotsDao;
import com.dizitiveit.pms.Dao.VehicleDetailsDao;
import com.dizitiveit.pms.Dao.VehicleMovementRegisterDao;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.Slots;
import com.dizitiveit.pms.model.VehicleDetails;
import com.dizitiveit.pms.model.VehicleMovementRegister;
import com.dizitiveit.pms.model.VisitorTagDetails;
import com.dizitiveit.pms.pojo.FlatVisitorSlotPojo;
import com.dizitiveit.pms.pojo.VehicleMovementRegisterPojo;
@RestController
@RequestMapping("/moment")
public class VehicleMomentRegisterController {

	@Autowired
	private VehicleDetailsDao vehicleDetailsDao;
	
	@Autowired
	private SlotsDao slotsDao;
	
	@Autowired
	private VehicleMovementRegisterDao vehicleMovementRegisterDao;
	
	@Autowired
	private ResponsesDao responsesDao;
	
	@PostMapping("/vehicleIn/{vehicleId}/{slotNo}")
	 public ResponseEntity<?> vehicleIn(@PathVariable long vehicleId,@PathVariable String slotNo){
		VehicleDetails vehicleDetails = vehicleDetailsDao.findByvehicleId(vehicleId);
		if(vehicleDetails!=null)
		{
			List<VehicleMovementRegister> vehicle = vehicleMovementRegisterDao.findByvehicleMovement(vehicleDetails.getVehicleId());
			System.out.println(vehicle.size());
		if(vehicle.size()>0) {
			List<VehicleMovementRegister> vehicleMovementRegister = vehicleMovementRegisterDao.findByvehicle(vehicleDetails.getVehicleId());
			if(vehicleMovementRegister.size()==0)
			{
				VehicleMovementRegister vehicleMovement = new VehicleMovementRegister();
				vehicleMovement.setVehicleDetails(vehicleDetails);
				vehicleMovement.setVehicleIn(new Date());
			Slots slots = slotsDao.findByslotNo(slotNo);
			vehicleMovement.setSlots(slots);
			System.out.println(vehicleMovement.toString());
			vehicleMovementRegisterDao.save(vehicleMovement);
	         if(slots.getBillingType()!=null)
	         { 	 
			slots.setFilled(true);
			slotsDao.save(slots);
	         }
	         else {
	        	 slots.setFilled(true);
					slots.setAssigned(true);
					slots.setFlats(vehicleMovement.getVehicleDetails().getFlats());
					slotsDao.save(slots);
	         }
				
	         Responses responses = responsesDao.findById(68);
				System.out.println("responseId" + responses.getResponsesId());
				System.out.println("resName" + responses.getResName());
				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
			}
			else {
				
				
				Responses responses = responsesDao.findById(71);
	 			System.out.println("responseId" + responses.getResponsesId());
	 			System.out.println("resName" + responses.getResName());
	 			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
			}
		}
		else{
			
			VehicleMovementRegister vehicleMovement = new VehicleMovementRegister();
			vehicleMovement.setVehicleDetails(vehicleDetails);
			vehicleMovement.setVehicleIn(new Date());
		Slots slots = slotsDao.findByslotNo(slotNo);
		vehicleMovement.setSlots(slots);
		System.out.println(vehicleMovement.toString());
		vehicleMovementRegisterDao.save(vehicleMovement);
		if(slots!= null)
        { 	 
		slots.setFilled(true);
		slotsDao.save(slots);
        }
        else {
       	 Responses responses = responsesDao.findById(69);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
        }
			 Responses responses = responsesDao.findById(68);
				System.out.println("responseId" + responses.getResponsesId());
				System.out.println("resName" + responses.getResName());
				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
			
		}
		
		}
		else {
			Responses responses = responsesDao.findById(72);
 			System.out.println("responseId" + responses.getResponsesId());
 			System.out.println("resName" + responses.getResName());
 			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		}
		
}
	
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		  long diffInMillies = date2.getTime() - date1.getTime();
		  
		  return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS); 
		  }

	@PostMapping("/vehicleOut/{vehicleMovementRegisterId}")
	 public ResponseEntity<?> vehicleOut(@PathVariable long vehicleMovementRegisterId){
		//VehicleDetails vehicleDetails = vehicleDetailsDao.findByvehicleId(vehicleId);
		VehicleMovementRegister vehicleMovement = vehicleMovementRegisterDao.findById(vehicleMovementRegisterId);
		//System.out.println(vehicleDetails);
		vehicleMovement.setVehicleOut(new Date());
		System.out.println(vehicleMovement);
		vehicleMovementRegisterDao.save(vehicleMovement);
		long difference_In_Time  = vehicleMovement.getVehicleOut().getTime() - vehicleMovement.getVehicleIn().getTime();
		//System.out.println(visitorOut.getOutTime());
		//System.out.println(visitorOut.getInTime());
		//long difference_In_Hours   = TimeUnit.MILLISECONDS.toHours(difference_In_Time) % 24; 
		// long difference_In_Hours = (difference_In_Time / (1000*60*60)) % 24; 
		  long hours = getDateDiff (vehicleMovement.getVehicleIn(), vehicleMovement.getVehicleOut(), TimeUnit.HOURS);
		  Slots slots = slotsDao.findByslotNo(vehicleMovement.getSlots().getSlotNo());
		  if(vehicleMovement.getSlots().getBillingType()!=null)
		  {
				slots.setFilled(false);
				slotsDao.save(slots);
		  }
		  else {
			  
			  slots.setFilled(false);
				slots.setAssigned(false);
				slots.setFlats(null);
				slotsDao.save(slots);
				
		 if(hours>=24) {
			 System.out.println("in if condition");
			 long diff_In_Days = Math.abs(vehicleMovement.getVehicleOut().getTime() - vehicleMovement.getVehicleIn().getTime());
			 long diff = TimeUnit.DAYS.convert(diff_In_Days, TimeUnit.MILLISECONDS);
			 System.out.println(diff);
			 double dayCost = 500;
			 double cost= diff* dayCost;
			// System.out.println(cost);
			 vehicleMovement.setParkingCost(cost);
			 vehicleMovementRegisterDao.save(vehicleMovement);
		 }
		 else if(hours>=3)
		 {
			 double actualCost = 50;	 
			 double cost= hours* actualCost;
			 vehicleMovement.setParkingCost(cost);
			 vehicleMovementRegisterDao.save(vehicleMovement);
		 }
		  }
		 Responses responses = responsesDao.findById(70);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
	}
	
	@GetMapping("/getVehicleIn")
	public ResponseEntity<?> getVehicleIn(){
		List<VehicleMovementRegister> vehicleMomentList = vehicleMovementRegisterDao.findByvehicleIn();
		List<VehicleMovementRegisterPojo> vehicleMovementPojolist = new ArrayList();
		for(VehicleMovementRegister vehicleMovement : vehicleMomentList )
		{
			VehicleMovementRegisterPojo vehicleMovementPojo = new VehicleMovementRegisterPojo();
			vehicleMovementPojo.setVehiclemovementId(vehicleMovement.getVehiclemovementId());
			vehicleMovementPojo.setRegNo(vehicleMovement.getVehicleDetails().getRegNo());
			vehicleMovementPojo.setColor(vehicleMovement.getVehicleDetails().getColor());
			vehicleMovementPojo.setVehicleId(vehicleMovement.getVehicleDetails().getVehicleId());
			vehicleMovementPojo.setModel(vehicleMovement.getVehicleDetails().getModel());
			vehicleMovementPojo.setMake(vehicleMovement.getVehicleDetails().getMake());
			vehicleMovementPojo.setFlatNo(vehicleMovement.getVehicleDetails().getFlats().getFlatNo());
			vehicleMovementPojo.setSlotNo(vehicleMovement.getSlots().getSlotNo());
			vehicleMovementPojo.setType(vehicleMovement.getVehicleDetails().getType());
			 DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	   			if(vehicleMovement.getVehicleIn()!=null) {
	   				vehicleMovementPojo.setVehicleIn((dfIn.format(vehicleMovement.getVehicleIn())));
					}
	   		 DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	   			if(vehicleMovement.getVehicleOut()!=null) {
	   				vehicleMovementPojo.setVehicleOut((dfOut.format(vehicleMovement.getVehicleOut())));
					}
	   			
	   			vehicleMovementPojo.setVehicleStatus(vehicleMovement.getVehicleDetails().isVehicleStatus());
	   			vehicleMovementPojolist.add(vehicleMovementPojo);
	   			
		}
		 HashMap<String,List<VehicleMovementRegisterPojo>> response = new HashMap<String,List<VehicleMovementRegisterPojo>>();
		 response.put("InVehicles",vehicleMovementPojolist);
		return ResponseEntity.ok(response);
	}
}
