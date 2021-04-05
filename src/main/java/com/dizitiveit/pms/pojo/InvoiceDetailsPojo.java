package com.dizitiveit.pms.pojo;

import java.util.Date;

import javax.persistence.ManyToOne;

import com.dizitiveit.pms.model.Flats;

import lombok.Data;
@Data
public class InvoiceDetailsPojo {
	
	private long  invoiceDetailsId;
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
    private String flatNo;
    private String createdAt;
    
    
}
