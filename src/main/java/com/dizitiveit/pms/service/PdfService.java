package com.dizitiveit.pms.service;

import java.io.ByteArrayInputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dizitiveit.pms.PdfGenerator;
import com.dizitiveit.pms.Dao.InvoiceItemsDao;
import com.dizitiveit.pms.model.InvoiceItems;

@Service
public class PdfService {
	
	@Autowired
	InvoiceItemsDao invoiceItemsDao;
	
	public ByteArrayInputStream loadbyLatest(long invoiceId) {
		InvoiceItems invoiceItems = invoiceItemsDao.findByInvoiceInvoiceId(invoiceId);
		System.out.println(invoiceItems.getDescription());
		ByteArrayInputStream in =PdfGenerator.invoicePDFReport(invoiceItems);
		 return in;
	}

}
