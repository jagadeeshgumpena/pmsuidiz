package com.dizitiveit.pms.pojo;

import java.util.List;

import lombok.Data;
@Data
public class InvoiceListPojo {

	private List<FlatInvoicePojo> flatInvoicePojolist;
	private List<InvoiceDetailsPojo> invoiceDetailsPojolist;
	private  FlatOwnersPojo flatResident;
	private double totalParkingCost;
	private long numberOfVisitors;
}
