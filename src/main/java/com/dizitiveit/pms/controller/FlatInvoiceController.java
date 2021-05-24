package com.dizitiveit.pms.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.Dao.FlatInvoiceDao;
import com.dizitiveit.pms.Dao.FlatOwnersDao;
import com.dizitiveit.pms.Dao.FlatResidenciesDao;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.GeneralInvoiceDao;
import com.dizitiveit.pms.Dao.InvoiceDetailsDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.Dao.VehicleMovementRegisterDao;
import com.dizitiveit.pms.Dao.VisitorParkingSlotsDao;
import com.dizitiveit.pms.Dao.VisitorTagDetailsDao;
import com.dizitiveit.pms.model.FlatInvoice;
import com.dizitiveit.pms.model.FlatOwners;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.GeneralInvoice;
import com.dizitiveit.pms.model.InvoiceDetails;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.VehicleMovementRegister;
import com.dizitiveit.pms.model.VisitorTagDetails;
import com.dizitiveit.pms.pojo.FlatInvoicePojo;
import com.dizitiveit.pms.pojo.FlatOwnersPojo;
import com.dizitiveit.pms.pojo.FlatResidenciesPojo;
import com.dizitiveit.pms.pojo.InvoiceDetailsPojo;
import com.dizitiveit.pms.pojo.InvoiceListPojo;
import com.dizitiveit.pms.pojo.InvoicePojo;
import com.dizitiveit.pms.pojo.VisitorTagDetailsPojo;
import com.dizitiveit.pms.pojo.VisitorsParkingCostPojo;

@RestController
@RequestMapping("/flatInvoice")
public class FlatInvoiceController {
	
	
	@Autowired
	private FlatsDao flatsDao;
	
	@Autowired
	private FlatInvoiceDao flatInvoiceDao;
	
	@Autowired
	private ResponsesDao responsesDao;
	
	@Autowired
	private InvoiceDetailsDao invoiceDetailsDao;
	
	@Autowired
	private FlatOwnersDao flatOwnersDao;
	
	@Autowired
	private FlatResidenciesDao flatResidenciesDao;
	
	@Autowired
	private VisitorTagDetailsDao visitorTagDetailsDao;
	
	@Autowired
	private GeneralInvoiceDao generalInvoiceDao;
	
	@Autowired
	private VehicleMovementRegisterDao vehicleMovementRegisterDao;

	@PostMapping("/saveFlatInvoice/{flatNo}")
	public ResponseEntity<?> saveFlatInvoice(@PathVariable String flatNo,@RequestBody FlatInvoice flatInvoice){
		Flats flats = flatsDao.findByflatNo(flatNo);
	  FlatInvoice flatInvoiceMonth = flatInvoiceDao.findByFlats(flats.getFlatId(), flatInvoice.getCreatedAt());
	  if(flatInvoiceMonth!=null)
	  {
		  flatInvoiceMonth.setCurrentBill(flatInvoice.getCurrentBill());
		  flatInvoiceMonth.setWaterBill(flatInvoice.getWaterBill());
		  flatInvoiceMonth.setParkingFee(flatInvoice.getParkingFee());
		  flatInvoiceDao.save(flatInvoiceMonth);
		  Responses responses = responsesDao.findById(58);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		  
	  }
	  else
	  {
		  Responses responses = responsesDao.findById(59);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		  
	  }
	  
	}
	
	@GetMapping("/getGeneralInvoiceByFlat/{flatNo}/{month}/{year}")
	public ResponseEntity<?>  getGeneralInvoiceByFlat(@PathVariable String flatNo,@PathVariable long month,@PathVariable long year){
		
			Flats flats = flatsDao.findByflatNo(flatNo);
			InvoicePojo invoice = new InvoicePojo();
			FlatInvoice flatInvoice = flatInvoiceDao.getInvoice(month, year, flats.getFlatId());
			if(flatInvoice!=null)
			{
			FlatInvoicePojo flatInvoicePojo = new FlatInvoicePojo();
			if(flatInvoice!=null) {
			flatInvoicePojo.setFlatNo(flatNo);
			flatInvoicePojo.setFlatInvoiceId(flatInvoice.getFlatInvoiceId());
			flatInvoicePojo.setGenerator(flatInvoice.getGenerator());
			flatInvoicePojo.setInfraStructure(flatInvoice.getInfraStructure());
			flatInvoicePojo.setSalary(flatInvoice.getSalary());
			flatInvoicePojo.setWater(flatInvoice.getWater());
			flatInvoicePojo.setElectricityBill(flatInvoice.getElectricityBill());
			DateFormat dfCreatedFlat = new SimpleDateFormat("yyyy-MM-dd");
				if(flatInvoice.getCreatedAt()!=null) {
					flatInvoicePojo.setCreatedAt((dfCreatedFlat.format(flatInvoice.getCreatedAt())));
				}
			}
			
			InvoiceDetails invoiceDetails = invoiceDetailsDao.getInvoiceDetails(month, year, flats.getFlatId());
			InvoiceDetailsPojo invoiceDetailsPojo = new InvoiceDetailsPojo();
			if(invoiceDetails!=null) {
		    invoiceDetailsPojo.setInvoiceDetailsId(invoiceDetails.getInvoiceDetailsId());
			invoiceDetailsPojo.setFlatNo(flatNo);
			invoiceDetailsPojo.setElectricityOpeningReading(invoiceDetails.getElectricityOpeningReading());
			invoiceDetailsPojo.setElectricityClosingReading(invoiceDetails.getElectricityClosingReading());
			invoiceDetailsPojo.setElectricityConsumedUnits(invoiceDetails.getElectricityConsumedUnits());
			invoiceDetailsPojo.setElectricityRsPerUnit(invoiceDetails.getElectricityRsPerUnit());
			invoiceDetailsPojo.setElectricityAmount(invoiceDetails.getElectricityAmount());
			invoiceDetailsPojo.setWaterOpeningReading(invoiceDetails.getWaterOpeningReading());
			invoiceDetailsPojo.setWaterClosingReading(invoiceDetails.getWaterClosingReading());
			invoiceDetailsPojo.setWaterConsumedUnits(invoiceDetails.getWaterConsumedUnits());
			invoiceDetailsPojo.setWaterRsPerUnit(invoiceDetails.getWaterRsPerUnit());
			invoiceDetailsPojo.setWaterAmount(invoiceDetails.getWaterAmount());
			
			DateFormat dfCreatedDetails = new SimpleDateFormat("yyyy-MM-dd");
			if(invoiceDetails.getCreatedAt()!=null) {
				invoiceDetailsPojo.setCreatedAt((dfCreatedDetails.format(invoiceDetails.getCreatedAt())));
			}
			}
			double totalParkingCost=0;
			long totalVistors=0;
			List<VisitorTagDetails> vistorList = visitorTagDetailsDao.getVisitorInvoice(month, year,  flats.getFlatId(),"OUT");
			totalVistors = vistorList.size();
			List<VehicleMovementRegister> vehicleMovementList = vehicleMovementRegisterDao.getVehicleMovementInvoice(month, year);
			totalVistors=totalVistors+vehicleMovementList.size();
			for(VehicleMovementRegister vehicleMovementRegister :vehicleMovementList )
			{
				if(vehicleMovementRegister.getVehicleDetails().getFlats().getFlatId()==flats.getFlatId())
				{
					totalParkingCost=totalParkingCost+vehicleMovementRegister.getParkingCost();
					
				}
			}
 			List<VisitorTagDetailsPojo> visitorListPojo = new ArrayList();
 			
 			VisitorsParkingCostPojo visitors = new VisitorsParkingCostPojo();
 			for( VisitorTagDetails visitor :  vistorList)
 			{
 				if(visitor.getParkingCost()!=0)
 				{
 					totalParkingCost= totalParkingCost+visitor.getParkingCost();
 					
 				}
 			}
 			visitors.setTotalParkingCost(totalParkingCost);
				visitors.setNumberOfVisitors(totalVistors);
 			
			FlatOwners flatOwners = flatOwnersDao.findByownersActive(flats.getFlatId(),true);
			FlatOwnersPojo flatResident= new FlatOwnersPojo();
			if(flatOwners!=null && flatOwners.getFlatResidencies()==null)
			{
			System.out.println("after owners dao");
			flatResident.setFlatNo(flats.getFlatNo());
			flatResident.setFirstname(flatOwners.getFirstname());
			flatResident.setLastName(flatOwners.getLastName());
			flatResident.setPhone(flatOwners.getPhone());
			flatResident.setEmail(flatOwners.getEmail());
				  DateFormat dfCreatedOwner = new SimpleDateFormat("yyyy-MM-dd");
	 				if(flatOwners.getCreatedAt()!=null) {
	 					flatResident.setCreatedAt((dfCreatedOwner.format(flatOwners.getCreatedAt())));
	 				}
	 		
		}
			else
			{
				flatResident.setFlatNo(flatOwners.getFlatResidencies().getFlats().getFlatNo());
				flatResident.setFirstname(flatOwners.getFlatResidencies().getFirstname());
				flatResident.setLastName(flatOwners.getFlatResidencies().getLastName());
				flatResident.setPhone(flatOwners.getFlatResidencies().getPhone());;
				flatResident.setEmail(flatOwners.getFlatResidencies().getEmail());
				  //flatResident.setTenantActive(flatOwners.getFlatResidencies().isTenantActive());
				  DateFormat dfCreatedTenant = new SimpleDateFormat("yyyy-MM-dd");
	 				if(flatOwners.getFlatResidencies().getCreatedAt()!=null) {
	 					flatResident.setCreatedAt((dfCreatedTenant.format(flatOwners.getFlatResidencies().getCreatedAt())));
	 				}
			}
			invoice.setInvoiceDetailsPojo(invoiceDetailsPojo);
			invoice.setFlatInvoicePojo(flatInvoicePojo);
			invoice.setFlatResident(flatResident);
			invoice.setVisitorsParkingCostPojo(visitors);
			HashMap<String, InvoicePojo> response = new HashMap<String,InvoicePojo>();
	         response.put("Invoice",invoice);
	         return ResponseEntity.ok(response);
			}
			else {
				Responses responses = responsesDao.findById(76);
				System.out.println("responseId" + responses.getResponsesId());
				System.out.println("resName" + responses.getResName());
				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
			}
			
	}
	
	@GetMapping("/getLatestFlatInvoice")
	public ResponseEntity<?> getLatestFlatInvoice(){
		 List<FlatInvoicePojo> invoices = new ArrayList();
		 List<GeneralInvoice> generalInvoiceList = generalInvoiceDao.findByLatest();
		 for(GeneralInvoice generalInvoice : generalInvoiceList )
		 {
			 List<FlatInvoice> flatInvoice = flatInvoiceDao.findBycreatedAt(generalInvoice.getCreatedAt());
			 if(flatInvoice.size()>0)
			 {
			 FlatInvoice flatInvoiceNew=flatInvoice.get(0);
			 FlatInvoicePojo flatInvoicepojo = new FlatInvoicePojo();
			 DateFormat dfCreatedFlat = new SimpleDateFormat("yyyy-MM-dd");
				if(flatInvoiceNew.getCreatedAt()!=null) {
					flatInvoicepojo.setCreatedAt((dfCreatedFlat.format(flatInvoiceNew.getCreatedAt())));
				}
			
			 flatInvoicepojo.setElectricityBill(flatInvoiceNew.getElectricityBill());
			 flatInvoicepojo.setFlatInvoiceId(flatInvoiceNew.getFlatInvoiceId());
			 flatInvoicepojo.setGenerator(flatInvoiceNew.getGenerator());
			 flatInvoicepojo.setInfraStructure(flatInvoiceNew.getInfraStructure());
			 flatInvoicepojo.setSalary(flatInvoiceNew.getSalary());
			 flatInvoicepojo.setWater(flatInvoiceNew.getWater());
			 invoices.add(flatInvoicepojo);
			 }
			 
		 }	 
		 HashMap<String, List<FlatInvoicePojo>> response = new HashMap<String,List<FlatInvoicePojo>>();
         response.put("FlatInvoicePojo",invoices);
	 return ResponseEntity.ok(response);
		 
	}
	
	@GetMapping("/getGeneralLastestInvoiceByFlat")
	public ResponseEntity<?>  getGeneralLastestInvoiceByFlat(){
		     List<Flats> flats= flatsDao.findAll();
		     List<InvoicePojo> invoices = new ArrayList();
		     for(Flats f : flats) {
			InvoicePojo invoice = new InvoicePojo();
			FlatInvoice flatInvoice = flatInvoiceDao.getInvoiceByFlat(f.getFlatId());
			FlatInvoicePojo flatInvoicePojo = new FlatInvoicePojo();
			if(flatInvoice!=null ) {	
			flatInvoicePojo.setFlatNo(f.getFlatNo());
			flatInvoicePojo.setFlatInvoiceId(flatInvoice.getFlatInvoiceId());
			flatInvoicePojo.setGenerator(flatInvoice.getGenerator());
			flatInvoicePojo.setInfraStructure(flatInvoice.getInfraStructure());
			flatInvoicePojo.setSalary(flatInvoice.getSalary());
			flatInvoicePojo.setWater(flatInvoice.getWater());
			flatInvoicePojo.setElectricityBill(flatInvoice.getElectricityBill());
			DateFormat dfCreatedFlat = new SimpleDateFormat("yyyy-MM-dd");
				if(flatInvoice.getCreatedAt()!=null) {
					flatInvoicePojo.setCreatedAt((dfCreatedFlat.format(flatInvoice.getCreatedAt())));
				}
				
				invoice.setFlatInvoicePojo(flatInvoicePojo);
				
			}
			InvoiceDetails invoiceDetails = invoiceDetailsDao.getLatestInvoiceByFlat(f.getFlatId());
			InvoiceDetailsPojo invoiceDetailsPojo = new InvoiceDetailsPojo();
			if(invoiceDetails!=null ) {
			invoiceDetailsPojo.setFlatNo(f.getFlatNo());
			invoiceDetailsPojo.setElectricityOpeningReading(invoiceDetails.getElectricityOpeningReading());
			invoiceDetailsPojo.setElectricityClosingReading(invoiceDetails.getElectricityClosingReading());
			invoiceDetailsPojo.setElectricityConsumedUnits(invoiceDetails.getElectricityConsumedUnits());
			invoiceDetailsPojo.setElectricityRsPerUnit(invoiceDetails.getElectricityRsPerUnit());
			invoiceDetailsPojo.setElectricityAmount(invoiceDetails.getElectricityAmount());
			invoiceDetailsPojo.setWaterOpeningReading(invoiceDetails.getWaterOpeningReading());
			invoiceDetailsPojo.setWaterClosingReading(invoiceDetails.getWaterClosingReading());
			invoiceDetailsPojo.setWaterConsumedUnits(invoiceDetails.getWaterConsumedUnits());
			invoiceDetailsPojo.setWaterRsPerUnit(invoiceDetails.getWaterRsPerUnit());
			invoiceDetailsPojo.setWaterAmount(invoiceDetails.getWaterAmount());
			
			DateFormat dfCreatedDetails = new SimpleDateFormat("yyyy-MM-dd");
			if(invoiceDetails.getCreatedAt()!=null) {
				invoiceDetailsPojo.setCreatedAt((dfCreatedDetails.format(invoiceDetails.getCreatedAt())));
			}
			invoice.setInvoiceDetailsPojo(invoiceDetailsPojo);
			}
			
			FlatOwners flatOwners = flatOwnersDao.findByownersActive(f.getFlatId(),true);
			FlatOwnersPojo flatResident= new FlatOwnersPojo();
			if(flatOwners!=null && flatOwners.getFlatResidencies()==null)
			{
			System.out.println("after owners dao");
			flatResident.setFlatNo(f.getFlatNo());
			flatResident.setFirstname(flatOwners.getFirstname());
			flatResident.setLastName(flatOwners.getLastName());
			flatResident.setPhone(flatOwners.getPhone());
			flatResident.setEmail(flatOwners.getEmail());
				  DateFormat dfCreatedOwner = new SimpleDateFormat("yyyy-MM-dd");
	 				if(flatOwners.getCreatedAt()!=null) {
	 					flatResident.setCreatedAt((dfCreatedOwner.format(flatOwners.getCreatedAt())));
	 				}
	 				invoice.setFlatResident(flatResident);
		}
			else if(flatOwners!=null && flatOwners.getFlatResidencies()!=null )
			{
				flatResident.setFlatNo(flatOwners.getFlatResidencies().getFlats().getFlatNo());
				flatResident.setFirstname(flatOwners.getFlatResidencies().getFirstname());
				flatResident.setLastName(flatOwners.getFlatResidencies().getLastName());
				flatResident.setPhone(flatOwners.getFlatResidencies().getPhone());;
				flatResident.setEmail(flatOwners.getFlatResidencies().getEmail());
				  //flatResident.setTenantActive(flatOwners.getFlatResidencies().isTenantActive());
				  DateFormat dfCreatedTenant = new SimpleDateFormat("yyyy-MM-dd");
	 				if(flatOwners.getFlatResidencies().getCreatedAt()!=null) {
	 					flatResident.setCreatedAt((dfCreatedTenant.format(flatOwners.getFlatResidencies().getCreatedAt())));
	 				}
	 				invoice.setFlatResident(flatResident);
			}
			
			if(flatInvoice!= null || invoiceDetails!=null)
			{
			invoices.add(invoice);
			}

		     }
		     HashMap<String, List<InvoicePojo>> response = new HashMap<String,List<InvoicePojo>>();
	         response.put("Invoice",invoices);
		 return ResponseEntity.ok(response);
	}
	
	@GetMapping("/getInvoiceHistory")
	public ResponseEntity<?> getInvoiceHistory(){
		 List<Flats> flats= flatsDao.findAll();
	     List<InvoiceListPojo> invoices = new ArrayList();
	     for(Flats f : flats) {
	    	 InvoiceListPojo invoice = new InvoiceListPojo();
	    	 List<FlatInvoice> flatInvoicelist = flatInvoiceDao.getByFlats(f.getFlatId());
	    	 List<FlatInvoicePojo> flatInvoicePojolist = new ArrayList();
	    	 for(FlatInvoice flatInvoice: flatInvoicelist) {
	    		 FlatInvoicePojo flatInvoicePojo = new FlatInvoicePojo();
	    		 flatInvoicePojo.setFlatNo(f.getFlatNo());
	 			flatInvoicePojo.setFlatInvoiceId(flatInvoice.getFlatInvoiceId());
	 			flatInvoicePojo.setGenerator(flatInvoice.getGenerator());
	 			flatInvoicePojo.setInfraStructure(flatInvoice.getInfraStructure());
	 			flatInvoicePojo.setSalary(flatInvoice.getSalary());
	 			flatInvoicePojo.setWater(flatInvoice.getWater());
	 			flatInvoicePojo.setElectricityBill(flatInvoice.getElectricityBill());
	 			DateFormat dfCreatedFlat = new SimpleDateFormat("yyyy-MM-dd");
	 				if(flatInvoice.getCreatedAt()!=null) {
	 					flatInvoicePojo.setCreatedAt((dfCreatedFlat.format(flatInvoice.getCreatedAt())));
	 				}
	 				flatInvoicePojolist.add(flatInvoicePojo);
	 				//invoice.setFlatInvoicePojolist(flatInvoicePojolist);
	    	 }
	    	 List<InvoiceDetails> invoiceDetailslist = invoiceDetailsDao.InvoiceByFlat(f.getFlatId());
	    	 List<InvoiceDetailsPojo> invoiceDetailsPojolist = new ArrayList();
	    	 for(InvoiceDetails invoiceDetails: invoiceDetailslist) {
	    		 InvoiceDetailsPojo invoiceDetailsPojo = new InvoiceDetailsPojo();
	    		 invoiceDetailsPojo.setInvoiceDetailsId(invoiceDetails.getInvoiceDetailsId());
	    		 invoiceDetailsPojo.setFlatNo(f.getFlatNo());
	 			invoiceDetailsPojo.setElectricityOpeningReading(invoiceDetails.getElectricityOpeningReading());
	 			invoiceDetailsPojo.setElectricityClosingReading(invoiceDetails.getElectricityClosingReading());
	 			invoiceDetailsPojo.setElectricityConsumedUnits(invoiceDetails.getElectricityConsumedUnits());
	 			invoiceDetailsPojo.setElectricityRsPerUnit(invoiceDetails.getElectricityRsPerUnit());
	 			invoiceDetailsPojo.setElectricityAmount(invoiceDetails.getElectricityAmount());
	 			invoiceDetailsPojo.setWaterOpeningReading(invoiceDetails.getWaterOpeningReading());
	 			invoiceDetailsPojo.setWaterClosingReading(invoiceDetails.getWaterClosingReading());
	 			invoiceDetailsPojo.setWaterConsumedUnits(invoiceDetails.getWaterConsumedUnits());
	 			invoiceDetailsPojo.setWaterRsPerUnit(invoiceDetails.getWaterRsPerUnit());
	 			invoiceDetailsPojo.setWaterAmount(invoiceDetails.getWaterAmount());
	 			
	 			DateFormat dfCreatedDetails = new SimpleDateFormat("yyyy-MM-dd");
	 			if(invoiceDetails.getCreatedAt()!=null) {
	 				invoiceDetailsPojo.setCreatedAt((dfCreatedDetails.format(invoiceDetails.getCreatedAt())));
	 			}
	 			invoiceDetailsPojolist.add(invoiceDetailsPojo);
	 			//invoice.setInvoiceDetailsPojolist(invoiceDetailsPojolist);
	    	 }
	    	 
	    	 FlatOwners flatOwners = flatOwnersDao.findByownersActive(f.getFlatId(),true);
				FlatOwnersPojo flatResident= new FlatOwnersPojo();
				if(flatOwners!=null && flatOwners.getFlatResidencies()==null)
				{
				System.out.println("after owners dao");
				flatResident.setFlatNo(f.getFlatNo());
				flatResident.setFirstname(flatOwners.getFirstname());
				flatResident.setLastName(flatOwners.getLastName());
				flatResident.setPhone(flatOwners.getPhone());
				flatResident.setEmail(flatOwners.getEmail());
					  DateFormat dfCreatedOwner = new SimpleDateFormat("yyyy-MM-dd");
		 				if(flatOwners.getCreatedAt()!=null) {
		 					flatResident.setCreatedAt((dfCreatedOwner.format(flatOwners.getCreatedAt())));
		 				}
		 		
			}
				else if(flatOwners!=null && flatOwners.getFlatResidencies()!=null )
				{
					flatResident.setFlatNo(flatOwners.getFlatResidencies().getFlats().getFlatNo());
					flatResident.setFirstname(flatOwners.getFlatResidencies().getFirstname());
					flatResident.setLastName(flatOwners.getFlatResidencies().getLastName());
					flatResident.setPhone(flatOwners.getFlatResidencies().getPhone());;
					flatResident.setEmail(flatOwners.getFlatResidencies().getEmail());
					  //flatResident.setTenantActive(flatOwners.getFlatResidencies().isTenantActive());
					  DateFormat dfCreatedTenant = new SimpleDateFormat("yyyy-MM-dd");
		 				if(flatOwners.getFlatResidencies().getCreatedAt()!=null) {
		 					flatResident.setCreatedAt((dfCreatedTenant.format(flatOwners.getFlatResidencies().getCreatedAt())));
		 				}
				}
				invoice.setFlatInvoicePojolist(flatInvoicePojolist);
				invoice.setInvoiceDetailsPojolist(invoiceDetailsPojolist);
				invoice.setFlatResident(flatResident);
				invoices.add(invoice);
	     }
	     HashMap<String, List<InvoiceListPojo>> response = new HashMap<String,List<InvoiceListPojo>>();
         response.put("Invoice",invoices);
	 return ResponseEntity.ok(response);
	}
	
	@GetMapping("retrieveInvoicebyLatest/{flatNo}/{month}/{year}")
	public ResponseEntity<?> retrieveInvoicebyLatest(@PathVariable String flatNo,@PathVariable long month,@PathVariable long year){
		 Flats flats = flatsDao.findByflatNo(flatNo); 
			InvoicePojo invoice = new InvoicePojo();
			FlatInvoice flatInvoice = flatInvoiceDao.getInvoiceByFlat(flats.getFlatId());
			FlatInvoicePojo flatInvoicePojo = new FlatInvoicePojo();
			if(flatInvoice!=null) {
			flatInvoicePojo.setFlatNo(flatNo);
			flatInvoicePojo.setFlatInvoiceId(flatInvoice.getFlatInvoiceId());
			flatInvoicePojo.setGenerator(flatInvoice.getGenerator());
			flatInvoicePojo.setInfraStructure(flatInvoice.getInfraStructure());
			flatInvoicePojo.setSalary(flatInvoice.getSalary());
			flatInvoicePojo.setWater(flatInvoice.getWater());
			flatInvoicePojo.setElectricityBill(flatInvoice.getElectricityBill());
			DateFormat dfCreatedFlat = new SimpleDateFormat("yyyy-MM-dd");
				if(flatInvoice.getCreatedAt()!=null) {
					flatInvoicePojo.setCreatedAt((dfCreatedFlat.format(flatInvoice.getCreatedAt())));
				}
			}
			else
			{
				 Responses responses = responsesDao.findById(59);
					System.out.println("responseId" + responses.getResponsesId());
					System.out.println("resName" + responses.getResName());
					return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
				
			}
			InvoiceDetails invoiceDetails = invoiceDetailsDao.getInvoiceByFlat(flats.getFlatId(),month, year);
			InvoiceDetailsPojo invoiceDetailsPojo = new InvoiceDetailsPojo();
			if(invoiceDetails!=null) {
			invoiceDetailsPojo.setFlatNo(flatNo);
			invoiceDetailsPojo.setElectricityOpeningReading(invoiceDetails.getElectricityOpeningReading());
			invoiceDetailsPojo.setElectricityClosingReading(invoiceDetails.getElectricityClosingReading());
			invoiceDetailsPojo.setElectricityConsumedUnits(invoiceDetails.getElectricityConsumedUnits());
			invoiceDetailsPojo.setElectricityRsPerUnit(invoiceDetails.getElectricityRsPerUnit());
			invoiceDetailsPojo.setElectricityAmount(invoiceDetails.getElectricityAmount());
			invoiceDetailsPojo.setWaterOpeningReading(invoiceDetails.getWaterOpeningReading());
			invoiceDetailsPojo.setWaterClosingReading(invoiceDetails.getWaterClosingReading());
			invoiceDetailsPojo.setWaterConsumedUnits(invoiceDetails.getWaterConsumedUnits());
			invoiceDetailsPojo.setWaterRsPerUnit(invoiceDetails.getWaterRsPerUnit());
			invoiceDetailsPojo.setWaterAmount(invoiceDetails.getWaterAmount());
			
			DateFormat dfCreatedDetails = new SimpleDateFormat("yyyy-MM-dd");
			if(invoiceDetails.getCreatedAt()!=null) {
				invoiceDetailsPojo.setCreatedAt((dfCreatedDetails.format(invoiceDetails.getCreatedAt())));
			}
			}
			else
			{
				 Responses responses = responsesDao.findById(59);
					System.out.println("responseId" + responses.getResponsesId());
					System.out.println("resName" + responses.getResName());
					return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
				
			}
			FlatOwners flatOwners = flatOwnersDao.findByownersActive(flats.getFlatId(),true);
			FlatOwnersPojo flatResident= new FlatOwnersPojo();
			if(flatOwners!=null && flatOwners.getFlatResidencies()==null)
			{
			System.out.println("after owners dao");
			flatResident.setFlatNo(flats.getFlatNo());
			flatResident.setFirstname(flatOwners.getFirstname());
			flatResident.setLastName(flatOwners.getLastName());
			flatResident.setPhone(flatOwners.getPhone());
			flatResident.setEmail(flatOwners.getEmail());
				  DateFormat dfCreatedOwner = new SimpleDateFormat("yyyy-MM-dd");
	 				if(flatOwners.getCreatedAt()!=null) {
	 					flatResident.setCreatedAt((dfCreatedOwner.format(flatOwners.getCreatedAt())));
	 				}
	 		
		}
			else
			{
				flatResident.setFlatNo(flatOwners.getFlatResidencies().getFlats().getFlatNo());
				flatResident.setFirstname(flatOwners.getFlatResidencies().getFirstname());
				flatResident.setLastName(flatOwners.getFlatResidencies().getLastName());
				flatResident.setPhone(flatOwners.getFlatResidencies().getPhone());;
				flatResident.setEmail(flatOwners.getFlatResidencies().getEmail());
				  //flatResident.setTenantActive(flatOwners.getFlatResidencies().isTenantActive());
				  DateFormat dfCreatedTenant = new SimpleDateFormat("yyyy-MM-dd");
	 				if(flatOwners.getFlatResidencies().getCreatedAt()!=null) {
	 					flatResident.setCreatedAt((dfCreatedTenant.format(flatOwners.getFlatResidencies().getCreatedAt())));
	 				}
			}
			invoice.setInvoiceDetailsPojo(invoiceDetailsPojo);
			invoice.setFlatInvoicePojo(flatInvoicePojo);
			invoice.setFlatResident(flatResident);
			
			HashMap<String, InvoicePojo> response = new HashMap<String,InvoicePojo>();
	         response.put("Invoice",invoice);
		 return ResponseEntity.ok(response);
			
	}
	
	@GetMapping("/InvoiceHistoryByFlat/{flatNo}")
	public ResponseEntity<?> InvoiceHistoryByFlat(@PathVariable String flatNo){
		 List<InvoiceListPojo> invoices = new ArrayList();
		 InvoiceListPojo invoice = new InvoiceListPojo();
		 Flats flats = flatsDao.findByflatNo(flatNo); 
    	 List<FlatInvoice> flatInvoicelist = flatInvoiceDao.getByFlats(flats.getFlatId());
    	 List<FlatInvoicePojo> flatInvoicePojolist = new ArrayList();
    	 for(FlatInvoice flatInvoice: flatInvoicelist) {
    		 FlatInvoicePojo flatInvoicePojo = new FlatInvoicePojo();
    		 flatInvoicePojo.setFlatNo(flatNo);
 			flatInvoicePojo.setFlatInvoiceId(flatInvoice.getFlatInvoiceId());
 			flatInvoicePojo.setGenerator(flatInvoice.getGenerator());
 			flatInvoicePojo.setInfraStructure(flatInvoice.getInfraStructure());
 			flatInvoicePojo.setSalary(flatInvoice.getSalary());
 			flatInvoicePojo.setWater(flatInvoice.getWater());
 			flatInvoicePojo.setElectricityBill(flatInvoice.getElectricityBill());
 			DateFormat dfCreatedFlat = new SimpleDateFormat("yyyy-MM-dd");
 				if(flatInvoice.getCreatedAt()!=null) {
 					flatInvoicePojo.setCreatedAt((dfCreatedFlat.format(flatInvoice.getCreatedAt())));
 				}
 				flatInvoicePojolist.add(flatInvoicePojo);
 				//invoice.setFlatInvoicePojolist(flatInvoicePojolist);
    	 }
    	 List<InvoiceDetails> invoiceDetailslist = invoiceDetailsDao.InvoiceByFlat(flats.getFlatId());
    	 List<InvoiceDetailsPojo> invoiceDetailsPojolist = new ArrayList();
    	 for(InvoiceDetails invoiceDetails: invoiceDetailslist) {
    		 InvoiceDetailsPojo invoiceDetailsPojo = new InvoiceDetailsPojo();
    		 invoiceDetailsPojo.setInvoiceDetailsId(invoiceDetails.getInvoiceDetailsId());
    		 invoiceDetailsPojo.setFlatNo(flats.getFlatNo());
 			invoiceDetailsPojo.setElectricityOpeningReading(invoiceDetails.getElectricityOpeningReading());
 			invoiceDetailsPojo.setElectricityClosingReading(invoiceDetails.getElectricityClosingReading());
 			invoiceDetailsPojo.setElectricityConsumedUnits(invoiceDetails.getElectricityConsumedUnits());
 			invoiceDetailsPojo.setElectricityRsPerUnit(invoiceDetails.getElectricityRsPerUnit());
 			invoiceDetailsPojo.setElectricityAmount(invoiceDetails.getElectricityAmount());
 			invoiceDetailsPojo.setWaterOpeningReading(invoiceDetails.getWaterOpeningReading());
 			invoiceDetailsPojo.setWaterClosingReading(invoiceDetails.getWaterClosingReading());
 			invoiceDetailsPojo.setWaterConsumedUnits(invoiceDetails.getWaterConsumedUnits());
 			invoiceDetailsPojo.setWaterRsPerUnit(invoiceDetails.getWaterRsPerUnit());
 			invoiceDetailsPojo.setWaterAmount(invoiceDetails.getWaterAmount());
 			
 			DateFormat dfCreatedDetails = new SimpleDateFormat("yyyy-MM-dd");
 			if(invoiceDetails.getCreatedAt()!=null) {
 				invoiceDetailsPojo.setCreatedAt((dfCreatedDetails.format(invoiceDetails.getCreatedAt())));
 			}
 			invoiceDetailsPojolist.add(invoiceDetailsPojo);
 			//invoice.setInvoiceDetailsPojolist(invoiceDetailsPojolist);
    	 }
    	 
    	 FlatOwners flatOwners = flatOwnersDao.findByownersActive(flats.getFlatId(),true);
			FlatOwnersPojo flatResident= new FlatOwnersPojo();
			if(flatOwners!=null && flatOwners.getFlatResidencies()==null)
			{
			System.out.println("after owners dao");
			flatResident.setFlatNo(flats.getFlatNo());
			flatResident.setFirstname(flatOwners.getFirstname());
			flatResident.setLastName(flatOwners.getLastName());
			flatResident.setPhone(flatOwners.getPhone());
			flatResident.setEmail(flatOwners.getEmail());
				  DateFormat dfCreatedOwner = new SimpleDateFormat("yyyy-MM-dd");
	 				if(flatOwners.getCreatedAt()!=null) {
	 					flatResident.setCreatedAt((dfCreatedOwner.format(flatOwners.getCreatedAt())));
	 				}
	 		
		}
			else if(flatOwners!=null && flatOwners.getFlatResidencies()!=null )
			{
				flatResident.setFlatNo(flatOwners.getFlatResidencies().getFlats().getFlatNo());
				flatResident.setFirstname(flatOwners.getFlatResidencies().getFirstname());
				flatResident.setLastName(flatOwners.getFlatResidencies().getLastName());
				flatResident.setPhone(flatOwners.getFlatResidencies().getPhone());;
				flatResident.setEmail(flatOwners.getFlatResidencies().getEmail());
				  //flatResident.setTenantActive(flatOwners.getFlatResidencies().isTenantActive());
				  DateFormat dfCreatedTenant = new SimpleDateFormat("yyyy-MM-dd");
	 				if(flatOwners.getFlatResidencies().getCreatedAt()!=null) {
	 					flatResident.setCreatedAt((dfCreatedTenant.format(flatOwners.getFlatResidencies().getCreatedAt())));
	 				}
			}
			invoice.setFlatInvoicePojolist(flatInvoicePojolist);
			invoice.setInvoiceDetailsPojolist(invoiceDetailsPojolist);
			invoice.setFlatResident(flatResident);
			invoices.add(invoice);
     
     HashMap<String, List<InvoiceListPojo>> response = new HashMap<String,List<InvoiceListPojo>>();
     response.put("Invoice",invoices);
    return ResponseEntity.ok(response);
	}


}
