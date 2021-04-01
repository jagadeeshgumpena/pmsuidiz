
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
  
 // @Table(name="invoiceDetails")
  
  @Data public class InvoiceDetails {
  
		
		  @Id
		  
		  @GeneratedValue(strategy = GenerationType.AUTO) 
		  private long invoiceDetailsId; 
		    private long electricityOpeningReading;
		    private long electricityClosingReading;
		    private long electricityConsumedUnits;
		    private double electricityRsPerUnit;
		    private double electricityAmount;
		    private long waterOpeningReading;
			  private long waterClosingReading;
			  private long waterConsumedUnits;
			  private double waterRsPerUnit; 
			  private double waterAmount;
		  @ManyToOne 
		  private Flats flats;
		  @DateTimeFormat(pattern="yyyy-MM-dd")
		  @Temporal(TemporalType.DATE)
		  private Date createdAt;
		 
  
  }
 
