package com.dizitiveit.pms.pojo;

import java.util.List;

import lombok.Data;
@Data
public class InvoiceHistoryPojo {

	private FlatInvoicePojo flatInvoicePojo;
	private InvoiceDetailsPojo invoiceDetailsPojo;
	private  FlatOwnersPojo flatResident;
	private double totalParkingCost;
	private long numberOfVisitors;
	private double invoiceAmount;
}
