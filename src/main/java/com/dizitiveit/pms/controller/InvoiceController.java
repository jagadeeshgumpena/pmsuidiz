package com.dizitiveit.pms.controller;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.PdfGenerator;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.InvoiceDao;
import com.dizitiveit.pms.Dao.InvoiceItemsDao;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.Invoice;
import com.dizitiveit.pms.model.InvoiceItems;
import com.dizitiveit.pms.model.VehicleDetails;
import com.dizitiveit.pms.service.PdfService;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceDao invoiceDao;
    
    @Autowired
    private InvoiceItemsDao invoiceItemsDao;
    
    @Autowired
    private PdfService pdfService;
    
    @Autowired
    private FlatsDao flatsDao;
    
	@GetMapping(value="/listInvoices/{flatNo}")
     public ResponseEntity<?> listInvoices(@PathVariable int flatNo){
		List<Invoice> listInvoice = invoiceDao.findByFlatsFlatNo(flatNo);
		HashMap<String, List<Invoice>> response = new HashMap<String,List<Invoice>>();
        response.put("listInvoice",listInvoice);
	 return ResponseEntity.ok(response);
     
	}
	
	@GetMapping(value="/viewInvoice/{invoiceId}")
	public ResponseEntity<?> viewInvoices(@PathVariable long invoiceId){
		InvoiceItems invoiceItems = invoiceItemsDao.findByInvoiceInvoiceId(invoiceId);
		return ResponseEntity.ok(invoiceItems);
		
	}
	
	@GetMapping(value="/retrieveInvoicebyLatest/{flatNo}")
	public ResponseEntity<?> retrieveInvoicebyLatest(@PathVariable int flatNo){
	//	List<Invoice> listInvoice = invoiceDao.findByInvoicecreatedAt(createdAt);
		 Flats flats = flatsDao.findByflatNo(flatNo); 
		java.util.Date date= new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		System.out.println(month);
		List<Invoice> listInvoice = invoiceDao.FindLatestInvoice(month,flats.getFlatId());
		
		HashMap<String, List<Invoice>> response = new HashMap<String,List<Invoice>>();
		response.put("listInvoice", listInvoice);
		 return ResponseEntity.ok(response);
	}
	
	@GetMapping(value="/getInvoicePdf/{invoiceId}")
	public ResponseEntity<?> getInvoicePdf(@PathVariable long invoiceId){
		InvoiceItems invoiceItems = invoiceItemsDao.findByInvoiceInvoiceId(invoiceId);
        ByteArrayInputStream bis = PdfGenerator.invoicePDFReport(invoiceItems);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoicesreport.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

	}
	
	@GetMapping(value="/getLastSixMonthsInvoices/{flatNo}")
	public ResponseEntity<?> getLastSixMonthsInvoices(@PathVariable int flatNo){
		 Flats flats = flatsDao.findByflatNo(flatNo); 
		List<Invoice> listInvoice = invoiceDao.GetLastSixMonthsInvoices(flats.getFlatId());
		HashMap<String, List<Invoice>> response = new HashMap<String,List<Invoice>>();
		response.put("listInvoice", listInvoice);
		return ResponseEntity.ok(response);
		
		
	}
	
	@GetMapping(value="/getInvoicesByMonthAndYear/{month}/{year}/{flatNo}")
	public ResponseEntity<?> getInvoicesByMonthAndYear(@PathVariable int month, @PathVariable int year,@PathVariable int flatNo) {
		Flats flats = flatsDao.findByflatNo(flatNo);
		List<Invoice> listInvoice = invoiceDao.getInvoicesByMonthAndYear(month, year,flats.getFlatId());
		HashMap<String, List<Invoice>> response = new HashMap<String,List<Invoice>>();
		response.put("listInvoice", listInvoice);
		return ResponseEntity.ok(response);
	}
	
	
}
