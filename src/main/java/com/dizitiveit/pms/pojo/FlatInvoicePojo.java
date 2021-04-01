package com.dizitiveit.pms.pojo;

import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.dizitiveit.pms.model.Flats;

import lombok.Data;
@Data
public class FlatInvoicePojo {

	private long flatInvoiceId;
	  private double electricityBill;
	  private double infraStructure;
	  private double water; 
	  private double salary;
	  private double generator;
	  private String createdAt;
	  private int flatNo;
	   
}
