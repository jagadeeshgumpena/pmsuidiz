package com.dizitiveit.pms.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.Dao.FlatInvoiceDao;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.GeneralInvoiceDao;
import com.dizitiveit.pms.Dao.InvoiceDetailsDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.model.FlatInvoice;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.GeneralInvoice;
import com.dizitiveit.pms.model.InvoiceDetails;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.pojo.InvoiceDetailsPojo;

@RestController
@RequestMapping("/invoiceDetails")
public class InvoiceDetailsController {

	@Autowired
	private GeneralInvoiceDao generalInvoiceDao;
	
	@Autowired
	private ResponsesDao responsesDao;
	
	@Autowired
	private FlatsDao flatsDao;
	
	@Autowired
	private InvoiceDetailsDao invoiceDetailsDao;
	
	@Autowired
	private FlatInvoiceDao flatInvoiceDao;
	
	
	  @PostMapping("/saveInvoiceDetails")
	  public ResponseEntity<?>saveInvoiceDetails(@RequestBody List<InvoiceDetailsPojo> invoiceDetailsListPojo) throws ParseException
	  { 
		  List<Integer> flatNos = new ArrayList();
		  String flatNostring="";
		  for(InvoiceDetailsPojo invoice:invoiceDetailsListPojo ) 
		  { 
			  System.out.println(invoice);
			 
			  Flats flats =flatsDao.findByflatNo(invoice.getFlatNo()); 
			  if(flats!=null)
			  {
			  InvoiceDetails invoiceDetails = new InvoiceDetails();
			  DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
			  Date startDate = df.parse(invoice.getCreatedAt());
			  LocalDate localDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				long month = localDate.getMonthValue();
				long year = localDate.getYear();
				System.out.println(year);
				System.out.println(month);
				InvoiceDetails invoiceDetailsMonth = invoiceDetailsDao.getInvoiceDetails(month, year, flats.getFlatId());
				if(invoiceDetailsMonth==null)
				{
	  invoiceDetails.setElectricityOpeningReading(invoice.getElectricityOpeningReading());
	  invoiceDetails.setElectricityClosingReading(invoice.getElectricityClosingReading());
	  invoiceDetails.setElectricityConsumedUnits(invoice.getElectricityConsumedUnits());
	  invoiceDetails.setElectricityRsPerUnit(invoice.getElectricityRsPerUnit());
	  invoiceDetails.setElectricityAmount(invoice.getElectricityAmount());
	  invoiceDetails.setWaterOpeningReading(invoice.getWaterOpeningReading());
	  invoiceDetails.setWaterClosingReading(invoice.getWaterClosingReading());
	  invoiceDetails.setWaterConsumedUnits(invoice.getWaterConsumedUnits());
	  invoiceDetails.setWaterRsPerUnit(invoice.getWaterRsPerUnit());
	  invoiceDetails.setWaterAmount(invoice.getWaterAmount());
	  invoiceDetails.setFlats(flats); 
	    SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd"); 
	    Date createdAt;
		try {
			createdAt = formatter.parse(invoice.getCreatedAt());
			invoiceDetails.setCreatedAt(createdAt);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	  invoiceDetailsDao.save(invoiceDetails);	
}	
				flatNostring=flatNostring+","+invoice.getFlatNo();
	  }
		  }			
		  
	  Responses responses = responsesDao.findById(58);
		System.out.println("responseId" + responses.getResponsesId());
		System.out.println("resName" + responses.getResName()); 
		return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName())); 
	  }
}
