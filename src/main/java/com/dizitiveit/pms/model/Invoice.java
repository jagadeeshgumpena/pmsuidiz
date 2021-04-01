package com.dizitiveit.pms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table(name="invoice")
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long  invoiceId;
	@ManyToOne
	private Flats flats;	
	private double total;
	private double tax;
	private double grandTotal;
	private String status;
	private Date createdAt;
	private Date updatedAt;
	 @Temporal(TemporalType.DATE)
	 private Date currDate;
}
