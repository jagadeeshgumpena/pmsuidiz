package com.dizitiveit.pms.controller;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.Dao.SlotsDao;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.Slots;
import com.dizitiveit.pms.model.VisitorParkingSlots;
import com.dizitiveit.pms.pojo.FlatOwnersPojo;
import com.dizitiveit.pms.pojo.FlatSlotsPojo;
import com.dizitiveit.pms.pojo.SlotsListPojo;
import com.dizitiveit.pms.pojo.SlotsPojo;

import lombok.experimental.PackagePrivate;

@RestController
@RequestMapping("/slots")
public class SlotsController {
	
	@Autowired
	private SlotsDao slotsDao;
	
	@Autowired
	private ResponsesDao responsesDao;

	@Autowired
	private FlatsDao flatsDao;
	
	@PostMapping("/saveSlots")
	public ResponseEntity<?> saveSlots(@RequestBody SlotsListPojo slotsListPojo){
		
		for(String slot:slotsListPojo.getSlotNo())
		{
			
		Slots slotsExisting = slotsDao.findByslotsNo(slot, slotsListPojo.getFloor());
		if(slotsExisting==null) {
			Slots slotsNew = new Slots();
			slotsNew.setCreatedAt(new Date());
			slotsNew.setSlotNo(slot);
			slotsNew.setFloor(slotsListPojo.getFloor());
			slotsNew.setBlock(slotsListPojo.getBlock());
			//slotsNew.setBillingType(slotsListPojo.getBillingType());
			slotsNew.setVehicleType(slotsListPojo.getVehicleType());
			slotsDao.save(slotsNew);
		}
		
		/*
		 * else { Responses responses = responsesDao.findById(27); return
		 * ResponseEntity.ok(new
		 * Responses(responses.getResponsesId(),responses.getResName())); }
		 */
		}
		Responses responses = responsesDao.findById(26);
		  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
	}
	
	@GetMapping("/getAllSLots")
	public ResponseEntity<?>  retriveSlotsByType(){
		List<Slots> listOfSlots = slotsDao.findAll();
		List<SlotsPojo> slotsPojo= new ArrayList();
		for(Slots slot : listOfSlots) 
		 {
			 SlotsPojo slotPojo= new SlotsPojo();
			 slotPojo.setSlotNo(slot.getSlotNo());
			 slotPojo.setFloor(slot.getFloor());
			 slotPojo.setVehicleType(slot.getVehicleType());
			 if(slot.getFlats()!=null) {
				 slotPojo.setFlatNo(slot.getFlats().getFlatNo());
				 }
			 slotPojo.setBlock(slot.getBlock());
			 slotPojo.setOccupied(slot.isOccupied());
			 slotPojo.setAssigned(slot.isAssigned());
			 slotPojo.setFilled(slot.isFilled());
			 slotPojo.setBillingType(slot.getBillingType());
			 slotsPojo.add(slotPojo);
	 }
		HashMap<String, List<SlotsPojo>> response = new HashMap<String,List<SlotsPojo>>();
		response.put("slotsPojo", slotsPojo);
	 return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/retriveSlotsByType/{type}")
	public ResponseEntity<?>  retriveSlotsByType(@PathVariable String type,@RequestParam (name = "value",required = false) String value){
		if(type.equalsIgnoreCase("floor")) {
			List<Slots> listOfSlots = slotsDao.findBySlotsInFloor(false, value);
			List<SlotsPojo> slotsPojo= new ArrayList();
			
			for(Slots slot : listOfSlots) 
			 {
				 SlotsPojo slotPojo= new SlotsPojo();
				 slotPojo.setSlotNo(slot.getSlotNo());
				 slotPojo.setFloor(slot.getFloor());
				 slotPojo.setVehicleType(slot.getVehicleType());
				 if(slot.getFlats()!=null) {
					 slotPojo.setFlatNo(slot.getFlats().getFlatNo());
					 }
				 slotPojo.setBlock(slot.getBlock());
				 slotPojo.setOccupied(slot.isOccupied());
				 slotPojo.setAssigned(slot.isAssigned());
				 slotPojo.setBillingType(slot.getBillingType());
				 slotsPojo.add(slotPojo);
		 }
			HashMap<String, List<SlotsPojo>> response = new HashMap<String,List<SlotsPojo>>();
			response.put("slotsPojo", slotsPojo);
		 return ResponseEntity.ok(response);
		}
		else if(type.equalsIgnoreCase("totalSlots")) {
			List<Slots> listOfSlots = slotsDao.findBySlots(false);
			List<SlotsPojo> slotsPojo= new ArrayList();
			for(Slots slot : listOfSlots) 
			 {
				
				 SlotsPojo slotPojo= new SlotsPojo();
				 slotPojo.setSlotNo(slot.getSlotNo());
				 slotPojo.setFloor(slot.getFloor());
				 slotPojo.setVehicleType(slot.getVehicleType());
				 if(slot.getFlats()!=null) {
				 slotPojo.setFlatNo(slot.getFlats().getFlatNo());
				 }
				 slotPojo.setBlock(slot.getBlock());
				 slotPojo.setOccupied(slot.isOccupied());
				 slotPojo.setAssigned(slot.isAssigned());
				 slotPojo.setBillingType(slot.getBillingType());
				 slotsPojo.add(slotPojo);
			 
		 }
			HashMap<String, List<SlotsPojo>> response = new HashMap<String,List<SlotsPojo>>();
			response.put("slotsPojo", slotsPojo);
		 return ResponseEntity.ok(response);
		}
		else if(type.equalsIgnoreCase("activeSlots")){
			List<Slots> listOfSlots = slotsDao.findBySlots(true);
			List<SlotsPojo> slotsPojo= new ArrayList();
			for(Slots slot : listOfSlots) 
			 {
				
				 SlotsPojo slotPojo= new SlotsPojo();
				 slotPojo.setSlotNo(slot.getSlotNo());
				 slotPojo.setFloor(slot.getFloor());
				 slotPojo.setVehicleType(slot.getVehicleType());
				 if(slot.getFlats()!=null) {
					 slotPojo.setFlatNo(slot.getFlats().getFlatNo());
					 }
				 slotPojo.setBlock(slot.getBlock());
				 slotPojo.setOccupied(slot.isOccupied());
				 slotPojo.setAssigned(slot.isAssigned());
				 slotPojo.setBillingType(slot.getBillingType());
				 slotsPojo.add(slotPojo);
			 
		 }
			HashMap<String, List<SlotsPojo>> response = new HashMap<String,List<SlotsPojo>>();
			response.put("slotsPojo", slotsPojo);
		 return ResponseEntity.ok(response);
			
		}
		else if(type.equalsIgnoreCase("activeSlotsForFloor")) {
			List<Slots> listOfSlots = slotsDao.findBySlotsInFloor(true,value);
			System.out.println(value);
			List<SlotsPojo> slotsPojo= new ArrayList();
			for(Slots slot : listOfSlots) 
			 {
				 SlotsPojo slotPojo= new SlotsPojo();
				 slotPojo.setSlotNo(slot.getSlotNo());
				 slotPojo.setFloor(slot.getFloor());
				 slotPojo.setVehicleType(slot.getVehicleType());
				 if(slot.getFlats()!=null) {
					 slotPojo.setFlatNo(slot.getFlats().getFlatNo());
					 }
				 slotPojo.setBlock(slot.getBlock());
				 slotPojo.setOccupied(slot.isOccupied());
				 slotPojo.setAssigned(slot.isAssigned());
				 slotPojo.setBillingType(slot.getBillingType());
				 slotsPojo.add(slotPojo);
			 
		 }
			HashMap<String, List<SlotsPojo>> response = new HashMap<String,List<SlotsPojo>>();
			response.put("slotsPojo", slotsPojo);
		 return ResponseEntity.ok(response);
		}
		else if(type.equalsIgnoreCase("slotNo")) {
			List<Slots> slots = slotsDao.getByslotNo(value);
			System.out.println(value);
			List<SlotsPojo> slotsPojo= new ArrayList();
			for(Slots slot : slots) 
			 {
				 SlotsPojo slotPojo= new SlotsPojo();
				 slotPojo.setSlotNo(slot.getSlotNo());
				 slotPojo.setFloor(slot.getFloor());
				 slotPojo.setVehicleType(slot.getVehicleType());
				 if(slot.getFlats()!=null) {
					 slotPojo.setFlatNo(slot.getFlats().getFlatNo());
					 }
				 slotPojo.setBlock(slot.getBlock());
				 slotPojo.setOccupied(slot.isOccupied());
				 slotPojo.setAssigned(slot.isAssigned());
				 slotPojo.setBillingType(slot.getBillingType());
				 slotsPojo.add(slotPojo);
			 }
			HashMap<String, List<SlotsPojo>> response = new HashMap<String,List<SlotsPojo>>();
			response.put("slotsPojo", slotsPojo);
		 return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok("error");
	}
	
	
	@PostMapping("/assignSlots/{flatNo}")
	public ResponseEntity<?> assignSlots(@RequestBody List<Slots> slots,@PathVariable String flatNo){
		
		 Flats flats =flatsDao.findByflatNo(flatNo);
		if(flats!=null)
		{
			
		for(Slots slotNo:slots)
		{
			Slots slotNew = slotsDao.findByslotNo(slotNo.getSlotNo());
			if(slotNew!=null) {
				slotNew.setFlats(flats);
				slotNew.setAssigned(true);
				slotNew.setBillingType("Flat");
				slotNew.setSlotNo(slotNo.getSlotNo());
				System.out.println(slotNew.toString());
				slotsDao.save(slotNew);
			}
		}
		Responses responses = responsesDao.findById(61);
		  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
          }
		else {
			Responses responses = responsesDao.findById(37);
			  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
		}
	}
	
	@PostMapping("/deactivateSlot/{slotNo}/{floor}")
	public ResponseEntity<?> deactivateSlot(@PathVariable String slotNo,@PathVariable String floor){
		Slots slots=slotsDao.findByslotsNo(slotNo, floor);
		if(slots!=null) {
			slots.setFlats(null);
			slots.setAssigned(false);
			slots.setOccupied(false);
			slotsDao.save(slots);
			Responses responses = responsesDao.findById(62);
			  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
		}
		else {
			
			Responses responses = responsesDao.findById(79);
			  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
		}
		
	}
	/*
	 * @GetMapping("/getSlotsByFlats/{flatNo}") public ResponseEntity<?>
	 * getSlotsByFlats(@PathVariable String flatNo){ Flats flats =
	 * flatsDao.findByflatNo(flatNo); SimpleDateFormat formatter= new
	 * SimpleDateFormat("yyyy-MM-dd"); Date date1 = new Date(); String
	 * date=formatter.format(date1); System.out.println(date); List<Slots> slotsFlat
	 * = slotsDao.getByflatNo(flats.getFlatId(), true,"Flat",date);
	 * System.out.println(date); System.out.println(slotsFlat.size());
	 * List<SlotsPojo> slotsFlatPojo= new ArrayList(); for(Slots slot : slotsFlat) {
	 * SlotsPojo slotPojo= new SlotsPojo(); slotPojo.setSlotNo(slot.getSlotNo());
	 * slotPojo.setFloor(slot.getFloor());
	 * slotPojo.setVehicleType(slot.getVehicleType()); if(slot.getFlats()!=null) {
	 * slotPojo.setFlatNo(slot.getFlats().getFlatNo()); }
	 * slotPojo.setBlock(slot.getBlock()); slotPojo.setOccupied(slot.isOccupied());
	 * slotPojo.setAssigned(slot.isAssigned());
	 * slotPojo.setBillingType(slot.getBillingType()); slotsFlatPojo.add(slotPojo);
	 * // System.out.println(slotsFlatPojo.toString());
	 * 
	 * } FlatSlotsPojo flatSlotPojo = new FlatSlotsPojo();
	 * flatSlotPojo.setFlatsPojo(slotsFlatPojo); //
	 * System.out.println(flatSlotPojo);
	 * 
	 * List<Slots> slotsVisitor =
	 * slotsDao.getByflatNo(flats.getFlatId(),true,"Visitor",date); List<SlotsPojo>
	 * slotsVisitorPojo= new ArrayList(); for(Slots slot : slotsVisitor) { SlotsPojo
	 * slotPojo= new SlotsPojo(); slotPojo.setSlotNo(slot.getSlotNo());
	 * slotPojo.setFloor(slot.getFloor());
	 * slotPojo.setVehicleType(slot.getVehicleType()); if(slot.getFlats()!=null) {
	 * slotPojo.setFlatNo(slot.getFlats().getFlatNo()); }
	 * slotPojo.setBlock(slot.getBlock()); slotPojo.setOccupied(slot.isOccupied());
	 * slotPojo.setAssigned(slot.isAssigned());
	 * slotPojo.setBillingType(slot.getBillingType());
	 * slotsVisitorPojo.add(slotPojo); }
	 * 
	 * flatSlotPojo.setVisitorPojo(slotsVisitorPojo); //
	 * System.out.println(slotsVisitorPojo);
	 * 
	 * HashMap<String, FlatSlotsPojo> response = new
	 * HashMap<String,FlatSlotsPojo>(); response.put("Visitorslots", flatSlotPojo);
	 * return ResponseEntity.ok(response);
	 * 
	 * }
	 */
}
