
  package com.dizitiveit.pms.model;
  
  import java.util.Date;

import javax.persistence.Column; import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
  
  @Entity 
  @Table(name="vehicle_details") 
  @Data
  public class VehicleDetails {
  
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private long vehicleId;
  
  @ManyToOne
  private Flats flats;
  
  private String type;
  
   private String regNo;
  
   private String rfidTag;
  
   private String color;
  
  private String make;
  
   private String model;
  
  private String status;
  @OneToOne
  private FlatDetails flatDetails;
 
  private Date createdAt;
	private Date updatedAt;

  
  }
 