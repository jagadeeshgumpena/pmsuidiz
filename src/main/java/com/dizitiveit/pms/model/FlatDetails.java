
  package com.dizitiveit.pms.model;
  
  import java.util.Date;

import javax.persistence.Column; import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import
  javax.persistence.Id; import javax.persistence.JoinColumn; import
  javax.persistence.OneToOne; import javax.persistence.Table;

import lombok.Data;
  
  @Entity
  @Table(name="flat_details")
  @Data
  public class FlatDetails {
  
  @Id 
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long flatdetailsId;
  
  @OneToOne
  private Flats flats;
 private Date createdAt;
 private Date updatedAt;
private String b1ParkingSlot;
private String b2ParkingSlot;
private String c1ParkingSlot;
private String c2ParkingSlot;
private boolean b1Occupied;
private boolean b2Occupied;
private boolean c1Occupied;
private boolean c2Occupied;
	
  }
 