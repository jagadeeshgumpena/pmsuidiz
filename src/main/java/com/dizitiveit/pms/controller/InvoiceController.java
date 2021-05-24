package com.dizitiveit.pms.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.dizitiveit.pms.PdfGenerator;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.InvoiceDao;
import com.dizitiveit.pms.Dao.InvoiceDetailsDao;
import com.dizitiveit.pms.Dao.InvoiceItemsDao;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.Invoice;
import com.dizitiveit.pms.model.InvoiceDetails;
import com.dizitiveit.pms.model.InvoiceItems;
import com.dizitiveit.pms.model.VehicleDetails;
import com.dizitiveit.pms.service.C07E04_CreateFromURL;
import com.dizitiveit.pms.service.PdfService;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.text.PageSize;




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
    

	@Autowired
	private InvoiceDetailsDao invoiceDetailsDao;
	
	public static final String ADDRESS = "http://localhost:8081/pdf/viewinvoice";

    
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
	public ResponseEntity<?> retrieveInvoicebyLatest(@PathVariable String flatNo){
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
	public ResponseEntity<?> getLastSixMonthsInvoices(@PathVariable String flatNo){
		 Flats flats = flatsDao.findByflatNo(flatNo); 
		List<Invoice> listInvoice = invoiceDao.GetLastSixMonthsInvoices(flats.getFlatId());
		HashMap<String, List<Invoice>> response = new HashMap<String,List<Invoice>>();
		response.put("listInvoice", listInvoice);
		return ResponseEntity.ok(response);
		
		
	}
	
	@GetMapping(value="/getInvoicesByMonthAndYear/{month}/{year}/{flatNo}")
	public ResponseEntity<?> getInvoicesByMonthAndYear(@PathVariable int month, @PathVariable int year,@PathVariable String flatNo) {
		Flats flats = flatsDao.findByflatNo(flatNo);
		List<Invoice> listInvoice = invoiceDao.getInvoicesByMonthAndYear(month, year,flats.getFlatId());
		HashMap<String, List<Invoice>> response = new HashMap<String,List<Invoice>>();
		response.put("listInvoice", listInvoice);
		return ResponseEntity.ok(response);
	}
	
	@RequestMapping(path = "/pdf/{flatNo}/{month}/{year}")
    public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response,@PathVariable String flatNo,@PathVariable long month,@PathVariable long year) throws IOException {

        /* Do Business Logic*/

		//long invoiceId=256;
		//InvoiceDetails invoiceDetails= invoiceDetailsDao.getInvoiceByFlat(invoiceId);
		//new C07E04_CreateFromURL().createPdf(new URL(ADDRESS), DEST);

        /* Create HTML using Thymeleaf template Engine */
		// servletContext = null;
       // WebContext context = new WebContext(request, response, servletContext);
        //context.setVariable("invoiceDetails", invoiceDetails);
        //TemplateEngine templateEngine= new TemplateEngine();
        //String orderHtml = templateEngine.process("invoiceDetails", context);

        /* Setup Source and target I/O streams */

        ByteArrayOutputStream target = new ByteArrayOutputStream();

        /*Setup converter properties. */
        //ConverterProperties converterProperties = new ConverterProperties();
        //converterProperties.setBaseUri("http://localhost:8081/pdf/viewinvoice");
        //PdfWriter writer = new PdfWriter("C:\\Users\\user\\Downloads\\output.pdf");
        //PdfDocument pdf = new PdfDocument(writer);
        
        ConverterProperties properties = new ConverterProperties();
        MediaDeviceDescription mediaDeviceDescription =
            new MediaDeviceDescription(MediaType.ALL_VALUE);
      
        properties.setMediaDeviceDescription(mediaDeviceDescription);
        //HtmlConverter.convertToPdf(new URL(ADDRESS).openStream(), pdf, properties);
        
       // String ADDRESSUrl = "http://localhost:8081/pdf/viewinvoice/"+flatNo+"/"+month+"/"+year;
        String ADDRESSUrl = "http://103.50.161.240:8080/pms-test/pdf/viewinvoice/"+flatNo+"/"+month+"/"+year;
        HtmlConverter.convertToPdf(new URL(ADDRESSUrl).openStream(), target,properties);
        /* extract output as bytes */
        byte[] bytes = target.toByteArray();


        /* Send the response as downloadable PDF */

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);

    }
	
	
	@RequestMapping(path = "/visitorsPdf/{flatNo}/{month}/{year}")
    public ResponseEntity<?> getvisitorsPdf(HttpServletRequest request, HttpServletResponse response,@PathVariable String flatNo,@PathVariable long month,@PathVariable long year) throws IOException {

        ByteArrayOutputStream target = new ByteArrayOutputStream();

    
        
        ConverterProperties properties = new ConverterProperties();
        MediaDeviceDescription mediaDeviceDescription =
            new MediaDeviceDescription(MediaType.ALL_VALUE);
      
        properties.setMediaDeviceDescription(mediaDeviceDescription);
      
        
       // String ADDRESSUrl = "http://localhost:8081/pdf/viewVisitorsList/"+flatNo+"/"+month+"/"+year;
        String ADDRESSUrl = "http://103.50.161.240:8080/pms-test/pdf/viewVisitorsList/"+flatNo+"/"+month+"/"+year;
        HtmlConverter.convertToPdf(new URL(ADDRESSUrl).openStream(), target,properties);
        /* extract output as bytes */
        byte[] bytes = target.toByteArray();


        /* Send the response as downloadable PDF */

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);

    }
	

}
