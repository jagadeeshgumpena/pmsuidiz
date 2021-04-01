package com.dizitiveit.pms.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.Dao.FlatInvoiceDao;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.GeneralInvoiceDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.model.FlatInvoice;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.GeneralInvoice;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.SecurityShifts;

@RestController
@RequestMapping("/generalInvoice")
public class GeneralInvoiceController {

	@Autowired
	private GeneralInvoiceDao generalInvoiceDao;
	
	@Autowired
	private ResponsesDao responsesDao;
	
	@Autowired
	private FlatsDao flatsDao;
	
	@Autowired
	private FlatInvoiceDao flatInvoiceDao;
	
	@PostMapping("/saveGeneralInvoice")
	public ResponseEntity<?> saveGeneralInvoice(@RequestBody GeneralInvoice generalInvoice){
		LocalDate localDate = generalInvoice.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		long month = localDate.getMonthValue();
		long year = localDate.getYear();
		System.out.println(year);
		System.out.println(month);
		GeneralInvoice generalInvoiceMonth = generalInvoiceDao.findByCreatedAt(month, year);
		if(generalInvoiceMonth==null) {
			Date date = new Date();
		//Calendar c = Calendar.getInstance();
		//generalInvoice.setMonth(c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
		generalInvoiceDao.save(generalInvoice);
		List<Flats> flatslist = flatsDao.findAll();
		int totalFlats = flatslist.size();
		System.out.println(totalFlats);
	
		Double electricityBill= generalInvoice.getElectricityBill()/totalFlats;
		Double electricityBillround = Math.round(electricityBill * 100.0) / 100.0;
		System.out.println(electricityBillround);
		Double generator=generalInvoice.getGenerator()/totalFlats;
		Double generatorround = Math.round(generator * 100.0) / 100.0;
		Double infraStructure = generalInvoice.getInfraStructure()/totalFlats;
		Double infraStructureround = Math.round(infraStructure * 100.0) / 100.0;
		Double salary=generalInvoice.getSalary()/totalFlats;
		Double salaryround = Math.round(salary * 100.0) / 100.0;
		Double water=generalInvoice.getWater()/totalFlats;
		Double waterround = Math.round(water * 100.0) / 100.0;
		for(Flats f : flatslist)
		{
			FlatInvoice flatInvoice = new FlatInvoice();
			flatInvoice.setElectricityBill(electricityBillround);
			flatInvoice.setGenerator(generatorround);
			flatInvoice.setInfraStructure(infraStructureround);
			flatInvoice.setSalary(salaryround);
			flatInvoice.setWater(waterround);
			flatInvoice.setCreatedAt(generalInvoice.getCreatedAt());
			flatInvoice.setFlats(f);
			flatInvoiceDao.save(flatInvoice);
		}
		 Responses responses = responsesDao.findById(55);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		}
		else {
			Responses responses = responsesDao.findById(57);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		}
	}
	
    @GetMapping("/viewGeneralInvoiceByDate/{month}/{year}") 
    public ResponseEntity<?> viewGeneralInvoiceByDate(@PathVariable long month,@PathVariable long year){ 
    	GeneralInvoice generalinvoice = generalInvoiceDao.findByCreatedAt(month, year); 
    	 HashMap<String, GeneralInvoice> response = new HashMap<String,GeneralInvoice>();
         response.put("generalinvoice",generalinvoice);
	 return ResponseEntity.ok(response);
    	}
	 
}
