package com.dizitiveit.pms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.kernel.pdf.PdfDocument; 


import com.dizitiveit.pms.model.InvoiceItems;

public class PdfGenerator {

	 private static Logger logger = LoggerFactory.getLogger(PdfGenerator.class);
	  
	  public static ByteArrayInputStream invoicePDFReport(InvoiceItems invoiceItems) {
		  
		// Creating a Document        
		
		/*
		 * Document document = new Document();
		 * 
		 * String dest = "C:\\Users\\user\\Desktop\\CLOUD COMPUTING\\invoice.pdf";
		 * PdfWriter writer = new PdfWriter(dest); PdfDocument pdf = new
		 * PdfDocument(writer);
		 */
	      
	      Document document = new Document();
	      
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
	        Font headFont2 = new Font(Font.FontFamily.TIMES_ROMAN, 18,
	                Font.BOLD);


	        try {
	        	Paragraph preface = new Paragraph();
	        	 preface.add(new Paragraph("Invoice", headFont2));
	        	 document.add(preface);
	        	 document.newPage();
	            PdfPTable table = new PdfPTable(6);
	            table.setWidthPercentage(100);
	            table.setWidths(new int[]{5,5,5,5,5,5});

	            //Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

	            PdfPCell hcell;
	            hcell = new PdfPCell(new Phrase("InvoiceId", headFont));
	            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(hcell);

	            hcell = new PdfPCell(new Phrase("CreatedAt", headFont));
	            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(hcell);

	            hcell = new PdfPCell(new Phrase("Description", headFont));
	            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(hcell);
	            
	            hcell = new PdfPCell(new Phrase("Tax", headFont));
	            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(hcell);

	            
	            hcell = new PdfPCell(new Phrase("GrandTotal", headFont));
	            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(hcell);


	            hcell = new PdfPCell(new Phrase("Value", headFont));
	            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(hcell);

	                PdfPCell cell;

	                cell = new PdfPCell(new Phrase(String.valueOf(invoiceItems.getInvoiceitemsId())));
	                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);

	                cell = new PdfPCell(new Phrase(String.valueOf(invoiceItems.getCreatedAt())));
	                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                cell.setPaddingRight(5);
	                table.addCell(cell);
	                
	                cell = new PdfPCell(new Phrase(String.valueOf(invoiceItems.getDescription())));
	                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                cell.setPaddingRight(5);
	                table.addCell(cell);

	                cell = new PdfPCell(new Phrase(String.valueOf(invoiceItems.getTax())));
	                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                cell.setPaddingRight(5);
	                table.addCell(cell);
	                
	                cell = new PdfPCell(new Phrase(String.valueOf(invoiceItems.getGrandTotal())));
	                cell.setPaddingLeft(5);
	                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	                table.addCell(cell);

	                
	                cell = new PdfPCell(new Phrase(String.valueOf(invoiceItems.getValue())));
	                cell.setColspan(3);
	                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                cell.setPaddingRight(5);
	                table.addCell(cell);
	                
	             
	          
	            
	            PdfPTable table1 = new PdfPTable(6);
	            table1.setWidthPercentage(100);
	            table1.setWidths(new int[]{5,5,5,5,5,5});

	            Font headFont1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

	            PdfPCell hcell1;
	            hcell1 = new PdfPCell(new Phrase("InvoiceId", headFont));
	            hcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table1.addCell(hcell1);

	            hcell1 = new PdfPCell(new Phrase("CreatedAt", headFont));
	            hcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table1.addCell(hcell1);

	            hcell1 = new PdfPCell(new Phrase("Description", headFont));
	            hcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table1.addCell(hcell1);
	            
	            hcell1 = new PdfPCell(new Phrase("Tax", headFont));
	            hcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table1.addCell(hcell1);

	            
	            hcell1 = new PdfPCell(new Phrase("GrandTotal", headFont));
	            hcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table1.addCell(hcell1);


	            hcell1 = new PdfPCell(new Phrase("Value", headFont));
	            hcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table1.addCell(hcell1);

	                PdfPCell cell1;

	                cell1 = new PdfPCell(new Phrase(String.valueOf(invoiceItems.getInvoiceitemsId())));
	                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table1.addCell(cell1);

	                cell1 = new PdfPCell(new Phrase(String.valueOf(invoiceItems.getCreatedAt())));
	                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                cell1.setPaddingRight(5);
	                table1.addCell(cell1);
	                
	                cell = new PdfPCell(new Phrase(String.valueOf(invoiceItems.getDescription())));
	                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                cell.setPaddingRight(5);
	                table1.addCell(cell1);

	                cell1 = new PdfPCell(new Phrase(String.valueOf(invoiceItems.getTax())));
	                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                cell1.setPaddingRight(5);
	                table1.addCell(cell1);
	                
	                cell = new PdfPCell(new Phrase(String.valueOf(invoiceItems.getGrandTotal())));
	                cell.setPaddingLeft(5);
	                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	                table1.addCell(cell1);

	                
	                cell1 = new PdfPCell(new Phrase(String.valueOf(invoiceItems.getValue())));
	                cell1.setColspan(3);
	                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                cell1.setPaddingRight(5);
	                table1.addCell(cell1);
	                
	             
	            
	                
		           // PdfWriter.getInstance(document, out);
		            document.open();
		            document.add(table);
		            document.add(table1);
	            
	            
	            document.close();
	            
	        } 
	        catch (DocumentException ex) {
	        
	            //Logger.getLogger(GeneratePdfReport.class.getName()).log(Level.SEVERE, null, ex);
	        }

	        return new ByteArrayInputStream(out.toByteArray());
	    }
}
