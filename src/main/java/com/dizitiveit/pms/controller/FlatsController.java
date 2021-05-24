package com.dizitiveit.pms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.aspectj.weaver.patterns.ConcreteCflowPointcut.Slot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.Dao.FlatDetailsDao;
import com.dizitiveit.pms.Dao.FlatOwnersDao;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.Dao.SlotsDao;
import com.dizitiveit.pms.Dao.TransactionsDao;
import com.dizitiveit.pms.model.FlatDetails;
import com.dizitiveit.pms.model.FlatOwners;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.Invoice;
import com.dizitiveit.pms.model.Residents;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.Slots;
import com.dizitiveit.pms.model.Transactions;
import com.dizitiveit.pms.model.VehicleDetails;
import com.dizitiveit.pms.model.VisitorTagDetails;
import com.dizitiveit.pms.pojo.SlotsListPojo;

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
	 
	 @Autowired
	 private FlatOwnersDao flatOwnersDao;
	 
	 
	 
	 @Autowired
	 private SlotsDao slotsDao;
	
	@GetMapping(value="/listflats")
	public ResponseEntity<?> listflats(){
		 List<String> flats = flatsDao.findAllFlats();		
		 HashMap<String, List<String>> response = new HashMap<String,List<String>>();
         response.put("listFlats",flats);
		 return ResponseEntity.ok(response);
	}
	
	@PostMapping("/saveFlats")
	public ResponseEntity<?> saveFlats(@RequestBody Flats flats){
		 Flats flatsExisting =flatsDao.findByflatNo(flats.getFlatNo());
		if(flatsExisting==null)
		{
			flats.setCreatedAt(new Date());	
		flatsDao.save(flats);
		Responses responses = responsesDao.findById(17);
		  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
		}
		else {
			Responses responses = responsesDao.findById(28);
			  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
		}
	}

	
	@GetMapping("/flatsList")
	public ResponseEntity<?> flatsList(){
		 List<Flats> flats = flatsDao.findAll();
		 List<Flats> flatsOcuppied = new ArrayList();
		 for(Flats flat : flats)
		 {
			 FlatOwners flatOwners = flatOwnersDao.findByownersActive(flat.getFlatId(),true);
			 if(flatOwners!=null)
			 {
				 flatsOcuppied.add(flat);
			 }
		 }
		 
		 HashMap<String, List<Flats>> response = new HashMap<String,List<Flats>>();
         response.put("listFlats",flatsOcuppied);
		 return ResponseEntity.ok(response);
	}
	
		
	@GetMapping(value="/listOfTransactions/{flatNo}")
	public ResponseEntity<?> listOfTransactions(@PathVariable String flatNo){
		 Flats flats = flatsDao.findByflatNo(flatNo); 
		 List<Transactions>listTransactions=transactionsDao.findByflatsFlatId(flats.getFlatId());
		 HashMap<String, List<Transactions>> response = new HashMap<String,List<Transactions>>();
			response.put("listTransactions", listTransactions);
			return ResponseEntity.ok(response);
		 
	}
}
