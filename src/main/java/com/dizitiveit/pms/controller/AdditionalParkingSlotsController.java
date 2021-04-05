package com.dizitiveit.pms.controller;

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

import com.dizitiveit.pms.Dao.AdditionalParkingSlotsDao;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.model.AdditionalParkingSlots;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.VisitorParkingSlots;
@RestController
@RequestMapping("/additionalSlots")
public class AdditionalParkingSlotsController {

	@Autowired
	private ResponsesDao responsesDao;
	@Autowired
	private AdditionalParkingSlotsDao additionalParkingSlotsDao;
	@Autowired
	private FlatsDao flatsDao;
	
	@PostMapping("/saveAdditionalSlotsForFlat/{flatNo}")
	public ResponseEntity<?> saveAdditionalSlotsForFlat(@RequestBody List<AdditionalParkingSlots> additionalParkingSlots,@PathVariable String flatNo){
		for(AdditionalParkingSlots v : additionalParkingSlots) 
		 {
			AdditionalParkingSlots additionalParkingSlotsExisting = additionalParkingSlotsDao.findByVehicleSlotNo(v.getVehicleSlotNo());
			if(additionalParkingSlotsExisting!=null)
			{
				System.out.println(v.getVehicleSlotNo());
				 Flats flats = flatsDao.findByflatNo(flatNo);
				 additionalParkingSlotsExisting.setSlotOccupied(true);
				 additionalParkingSlotsExisting.setFlats(flats);
				additionalParkingSlotsDao.save(additionalParkingSlotsExisting);
				Responses responses = responsesDao.findById(61);
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
		return ResponseEntity.ok("error");
	}
	
	@PostMapping("/saveAdditionalSlots/{vehicleType}")
	public ResponseEntity<?> saveAdditionalSlots(@RequestBody List<AdditionalParkingSlots> additionalParkingSlots,@PathVariable String vehicleType){
		for(AdditionalParkingSlots v : additionalParkingSlots) 
		 {
			AdditionalParkingSlots additionalParkingSlotsExisting = additionalParkingSlotsDao.findByVehicleSlotNo(v.getVehicleSlotNo());;
			if(additionalParkingSlotsExisting==null)
			{
				v.setVehicleType(vehicleType);
				additionalParkingSlotsDao.save(v);
			}
		
		 }
		
		 Responses responses = responsesDao.findById(62);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
	}
	
	@GetMapping(value="/listOfAdditionalVehicleSlots")
	public ResponseEntity<?>  saveVisitorSlots(){
		List<AdditionalParkingSlots> listOfSlots = additionalParkingSlotsDao.findByslotOccupied(false);
		 HashMap<String, List<AdditionalParkingSlots>> response = new HashMap<String,List<AdditionalParkingSlots>>();
			response.put("listOfVehicleParkingSlots", listOfSlots);
			return ResponseEntity.ok(response);
	}
}
