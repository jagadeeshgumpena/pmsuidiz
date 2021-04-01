package com.dizitiveit.pms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
@Entity
@Table(name="invoiceItems")
@Data
public class InvoiceItems {
	@Id
	private long invoiceitemsId;
	@OneToOne
	private Invoice invoice;
	private String description;
	private double value;
	private double tax;
	private double grandTotal;
	@Temporal(TemporalType.DATE)
	private Date createdAt;
	private Date updatedAt;
}
