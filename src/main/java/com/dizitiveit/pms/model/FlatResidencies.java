
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
  @Table(name="flat_residencies") 
  @Data
  public class FlatResidencies {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long flatresidenciesId;
  @OneToOne
  private Flats flats;
   private String firstname;
   private String lastName; 
  private String email;
  private String phone;
  private boolean owner;
  private Date createdAt;
	private Date updatedAt;
	private boolean tenantActive;
  
  
  }
 