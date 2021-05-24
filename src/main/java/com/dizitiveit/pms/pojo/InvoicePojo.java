package com.dizitiveit.pms.pojo;

import java.util.List;

import com.dizitiveit.pms.model.GeneralInvoice;
import com.dizitiveit.pms.model.InvoiceDetails;

import lombok.Data;

@Data
public class InvoicePojo {

	private FlatInvoicePojo flatInvoicePojo;
	private InvoiceDetailsPojo invoiceDetailsPojo;
	private  FlatOwnersPojo flatResident;
	private VisitorsParkingCostPojo visitorsParkingCostPojo;
	
}
