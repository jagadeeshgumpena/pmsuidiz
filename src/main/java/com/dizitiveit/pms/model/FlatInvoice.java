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

@Data
@Entity
 @Table(name="flat_invoice")
public class FlatInvoice {

	 @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private long flatInvoiceId;
	  private double electricityBill;
	  private double infraStructure;
	  private double water; 
	  private double salary;
	  private double generator;
	  @DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	  private Date createdAt;
	  @ManyToOne
	  private Flats flats;
	  private double parkingFee;
	  private double currentBill;
	  private double waterBill;
	  private double total; 
	  
	  
}
