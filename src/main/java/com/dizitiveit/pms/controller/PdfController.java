package com.dizitiveit.pms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dizitiveit.pms.Dao.FlatInvoiceDao;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.GeneralInvoiceDao;
import com.dizitiveit.pms.Dao.InvoiceDetailsDao;
import com.dizitiveit.pms.model.FlatInvoice;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.GeneralInvoice;
import com.dizitiveit.pms.model.InvoiceDetails;

@Controller
@RequestMapping("/pdf")
public class PdfController {


	@Autowired
	private InvoiceDetailsDao invoiceDetailsDao;
	
	@Autowired
	private FlatsDao flatsDao; 
	
	@Autowired
	private GeneralInvoiceDao generalInvoiceDao;
	
	@Autowired
	private FlatInvoiceDao flatInvoiceDao;
	
	@RequestMapping(value = "/viewinvoice/{flatNo}")
	public String viewinvoice(Model model,@PathVariable String flatNo) {
		System.out.println("In method");
		System.out.println(flatNo);
		//long invoiceId=256;
		 Flats flats = flatsDao.findByflatNo(flatNo); 
		InvoiceDetails invoiceDetails= invoiceDetailsDao.getInvoiceByFlat(flats.getFlatId());
		model.addAttribute("invoiceDetails",invoiceDetails);
		FlatInvoice flatInvoice = flatInvoiceDao.getInvoiceByFlat(flats.getFlatId());
		model.addAttribute("flatInvoice",flatInvoice);
		GeneralInvoice generalInvoice = generalInvoiceDao.findBycreatedAt(flatInvoice.getCreatedAt());
		double total=generalInvoice.getElectricityBill()+generalInvoice.getGenerator()+generalInvoice.getInfraStructure()+generalInvoice.getSalary()+generalInvoice.getWater();
		model.addAttribute("total",total);
		model.addAttribute("generalInvoice",generalInvoice);
		double overAlltotal =invoiceDetails.getElectricityAmount()+invoiceDetails.getWaterAmount()+total;
		model.addAttribute("overAlltotal",overAlltotal);
		double GstAmount = overAlltotal*18/100;
		model.addAttribute("GstAmount",GstAmount);
		System.out.println(GstAmount);
		double invoiceAmount = overAlltotal+GstAmount;
		model.addAttribute("invoiceAmount",invoiceAmount);
     	return "pdf";	
	}
	
	
}
