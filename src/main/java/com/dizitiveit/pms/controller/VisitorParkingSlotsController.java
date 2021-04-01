package com.dizitiveit.pms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.Dao.VisitorParkingSlotsDao;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.Transactions;
import com.dizitiveit.pms.model.VehicleDetails;
import com.dizitiveit.pms.model.VisitorParkingSlots;

@RestController
@RequestMapping("/visitorParking")
public class VisitorParkingSlotsController {

	@Autowired
	private VisitorParkingSlotsDao visitorParkingSlotsDao;
	
	@Autowired
	private ResponsesDao responsesDao;
	
	@PostMapping(value="/saveVisitorSlots/{vehicleType}")
	public ResponseEntity<?> saveVisitorSlots(@RequestBody List<VisitorParkingSlots> visitorParkingSlots,@PathVariable String vehicleType){
			
			for(VisitorParkingSlots v : visitorParkingSlots) 
			 {
				VisitorParkingSlots visitorParkingSlotsExisting = visitorParkingSlotsDao.findBySlotNo(v.getSlotNo());
				if(visitorParkingSlotsExisting==null)
				{
					v.setSlotCostPerHour(50);
					v.setVehicleType(vehicleType);
					visitorParkingSlotsDao.save(v);
				}
			
			 }
			Responses responses = responsesDao.findById(47);
			  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
		}
		
	
	@GetMapping(value="/listOfVisitorSlots")
	public ResponseEntity<?>  saveVisitorSlots(){
		List<VisitorParkingSlots> listOfSlots = visitorParkingSlotsDao.findByslotOccupied(false);
		 HashMap<String, List<VisitorParkingSlots>> response = new HashMap<String,List<VisitorParkingSlots>>();
			response.put("listOfParkingSlots", listOfSlots);
			return ResponseEntity.ok(response);
	}
	
	@GetMapping(value="/listOfVisitorSlotsTrue")
	public ResponseEntity<?>  listOfVisitorSlotsTrue(){
		List<VisitorParkingSlots> listOfSlots = visitorParkingSlotsDao.findByslotOccupied(true);
		 HashMap<String, List<VisitorParkingSlots>> response = new HashMap<String,List<VisitorParkingSlots>>();
			response.put("listOfParkingSlots", listOfSlots);
			return ResponseEntity.ok(response);
	}
	
	
}
