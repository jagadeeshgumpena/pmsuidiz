package com.dizitiveit.pms.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dizitiveit.pms.Dao.FlatInvoiceDao;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.GeneralInvoiceDao;
import com.dizitiveit.pms.Dao.InvoiceDetailsDao;
import com.dizitiveit.pms.Dao.VehicleMovementRegisterDao;
import com.dizitiveit.pms.Dao.VisitorTagDetailsDao;
import com.dizitiveit.pms.model.FlatInvoice;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.GeneralInvoice;
import com.dizitiveit.pms.model.InvoiceDetails;
import com.dizitiveit.pms.model.VehicleMovementRegister;
import com.dizitiveit.pms.model.VisitorTagDetails;
import com.dizitiveit.pms.pojo.VisitorTagDetailsPojo;

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
	
	@Autowired
	private VisitorTagDetailsDao visitorTagDetailsDao;
	
	@Autowired
	private VehicleMovementRegisterDao vehicleMovementRegisterDao;
	
	@RequestMapping(value = "/viewinvoice/{flatNo}/{month}/{year}")
	public String viewinvoice(Model model,@PathVariable String flatNo,@PathVariable long month,@PathVariable long year) {
		System.out.println("In method");
		System.out.println(flatNo);
		//long invoiceId=256;
		 Flats flats = flatsDao.findByflatNo(flatNo); 
		InvoiceDetails invoiceDetails= invoiceDetailsDao.getInvoiceByFlat(flats.getFlatId(),month, year);
		model.addAttribute("invoiceDetails",invoiceDetails);
		FlatInvoice flatInvoice = flatInvoiceDao.getInvoiceByFlat(flats.getFlatId());
		model.addAttribute("flatInvoice",flatInvoice);
		GeneralInvoice generalInvoice = generalInvoiceDao.findBycreatedAt(flatInvoice.getCreatedAt());
		System.out.println(flatInvoice.getCreatedAt());
		//Calendar cal = Calendar.getInstance();
		//cal.setTime(flatInvoice.getCreatedAt());
		//int month = cal.get(Calendar.MONTH);
		//int year = cal.get(Calendar.YEAR);
		List<VisitorTagDetails> vistorList = visitorTagDetailsDao.getVisitorInvoice(month, year,  flats.getFlatId(),"OUT");
		int visitorsCount=vistorList.size();
		double totalParkingCost=0;
		List<VehicleMovementRegister> vehicleMovementList = vehicleMovementRegisterDao.getVehicleMovementInvoice(month, year);
		visitorsCount=visitorsCount+vehicleMovementList.size();
		for(VehicleMovementRegister vehicleMovementRegister :vehicleMovementList )
		{
			if(vehicleMovementRegister.getVehicleDetails().getFlats().getFlatId()==flats.getFlatId())
			{
				totalParkingCost=totalParkingCost+vehicleMovementRegister.getParkingCost();
				
			}
		}
		
		for( VisitorTagDetails visitor :  vistorList)
		{
		 	totalParkingCost= totalParkingCost+visitor.getParkingCost();
	}	

		
		
		double total=generalInvoice.getElectricityBill()+generalInvoice.getGenerator()+generalInvoice.getInfraStructure()+generalInvoice.getSalary()+generalInvoice.getWater();
		model.addAttribute("total",total);
		model.addAttribute("generalInvoice",generalInvoice);
		double overAlltotal = 0;
		if(invoiceDetails!=null)
		{
			overAlltotal=invoiceDetails.getElectricityAmount()+invoiceDetails.getWaterAmount()+total;
		}
		model.addAttribute("overAlltotal",overAlltotal);
		double GstAmount = overAlltotal*18/100;
		model.addAttribute("GstAmount",GstAmount);
		System.out.println(GstAmount);
		double invoiceAmount = overAlltotal+GstAmount;
		model.addAttribute("invoiceAmount",invoiceAmount);
		model.addAttribute("visitorsCount",visitorsCount);
		model.addAttribute("totalParkingCost",totalParkingCost);
     	return "pdf";	
	}
	
	@RequestMapping(value="/viewVisitorsList/{flatNo}/{month}/{year}") 
	public String viewWorknotesHis(Model model,@PathVariable String flatNo,@PathVariable long month,@PathVariable long year) {
		  Flats flats = flatsDao.findByflatNo(flatNo);
		List<VisitorTagDetails> vistorList = visitorTagDetailsDao.getVisitorInvoice(month, year,  flats.getFlatId(),"OUT");
		System.out.println(vistorList.size());
		List<VisitorTagDetailsPojo> visitorListPojo = new ArrayList();
		double totalParkingCost=0;
			for( VisitorTagDetails visitor :  vistorList)
			{
				VisitorTagDetailsPojo visitorPojo = new VisitorTagDetailsPojo();
				visitorPojo.setBlockNumber(visitor.getBlockNumber());
				visitorPojo.setBrand(visitor.getBrand());
				DateFormat dfExpectedIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
			if(visitor.getExpectedInTime()!=null) {
				visitorPojo.setExpectedInTime((dfExpectedIn.format(visitor.getExpectedInTime())));
			}
			DateFormat dfExpectedOut= new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
			if(visitor.getExpectedOutTime()!=null) {
				visitorPojo.setExpectedOutTime((dfExpectedOut.format(visitor.getExpectedOutTime())));
			}
			    visitorPojo.setFlatNo(flatNo);
				visitorPojo.setModel(visitor.getModel());
				 DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if(visitor.getInTime()!=null) {
					visitorPojo.setInTime((dfIn.format(visitor.getInTime())));
			}

			DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
			if(visitor.getOutTime()!=null) {
				visitorPojo.setOutTime((dfOut.format(visitor.getOutTime())));
			}
				visitorPojo.setParkingCost(visitor.getParkingCost());
				if(visitor.getSlots()!=null)
				{	
				visitorPojo.setSlotNo(visitor.getSlots().getSlotNo());
				}
				visitorPojo.setPhoneNumber(visitor.getPhoneNumber());
				visitorPojo.setPurpose(visitor.getPurpose());
				//visitorPojo.setResidentType(visitor.getResidentType());
				visitorPojo.setVehicleNumber(visitor.getVehicleNumber());
				visitorPojo.setVehicleType(visitor.getVehicleType());
				visitorPojo.setVisitorId(visitor.getVisitorId());
				visitorPojo.setVisitorName(visitor.getVisitorName());
				visitorPojo.setVisitorStatus(visitor.getVisitorStatus());
				visitorPojo.setType(visitor.getType());
				
			 	totalParkingCost= totalParkingCost+visitor.getParkingCost();
				model.addAttribute("totalParkingCost",totalParkingCost);
				
				visitorListPojo.add(visitorPojo);

		}	
	  model.addAttribute("visitorListPojo",visitorListPojo); 
	
	  return "VisitorsPdf"; 
	  }
	
}
