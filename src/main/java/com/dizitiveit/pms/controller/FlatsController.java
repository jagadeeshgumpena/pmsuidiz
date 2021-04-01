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

import com.dizitiveit.pms.Dao.FlatDetailsDao;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.Dao.TransactionsDao;
import com.dizitiveit.pms.model.FlatDetails;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.Invoice;
import com.dizitiveit.pms.model.Residents;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.Transactions;
import com.dizitiveit.pms.model.VehicleDetails;
import com.dizitiveit.pms.model.VisitorTagDetails;

@RestController
@RequestMapping("/flats")
public class FlatsController {

	@Autowired
	private FlatsDao flatsDao;
	
	@Autowired
	private TransactionsDao transactionsDao;
	
	@Autowired
	private ResponsesDao responsesDao;
	
	 @Autowired
	   private FlatDetailsDao flatDetailsDao;
	
	@GetMapping(value="/listflats")
	public ResponseEntity<?> listflats(){
		 List<Integer> flats = flatsDao.findAllFlats();		
		 HashMap<String, List<Integer>> response = new HashMap<String,List<Integer>>();
         response.put("listFlats",flats);
		 return ResponseEntity.ok(response);
	}
	
	@GetMapping("/flatsList")
	public ResponseEntity<?> flatsList(){
		 List<Flats> flats = flatsDao.findAll();		
		 HashMap<String, List<Flats>> response = new HashMap<String,List<Flats>>();
         response.put("listFlats",flats);
		 return ResponseEntity.ok(response);
	}
	
	@PostMapping("/saveFlats")
	public ResponseEntity<?> saveFlats(@RequestBody Flats flats){
		 Flats flatsExisting =flatsDao.findByflatNo(flats.getFlatNo());
		if(flatsExisting==null) {
		  flatsDao.save(flats);
		  FlatDetails flatDetails = new FlatDetails();
		  flatDetails.setB1ParkingSlot(flats.getFlatNo()+"b1");
		  flatDetails.setB2ParkingSlot(flats.getFlatNo()+"b2");
		  flatDetails.setC1ParkingSlot(flats.getFlatNo()+"c1");
		  flatDetails.setC2ParkingSlot(flats.getFlatNo()+"c2");
		  flatDetails.setFlats(flats);
		  flatDetailsDao.save(flatDetails);
		Responses responses = responsesDao.findById(26);
		  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
		}
		else {
			Responses responses = responsesDao.findById(27);
			  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
		}
	}
	
	@GetMapping(value="/listOfTransactions/{flatNo}")
	public ResponseEntity<?> listOfTransactions(@PathVariable int flatNo){
		 Flats flats = flatsDao.findByflatNo(flatNo); 
		 List<Transactions>listTransactions=transactionsDao.findByflatsFlatId(flats.getFlatId());
		 HashMap<String, List<Transactions>> response = new HashMap<String,List<Transactions>>();
			response.put("listTransactions", listTransactions);
			return ResponseEntity.ok(response);
		 
	}
}
