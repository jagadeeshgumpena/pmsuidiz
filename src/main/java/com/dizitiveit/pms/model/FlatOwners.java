
  package com.dizitiveit.pms.model;
  
  import java.util.Date;

import javax.persistence.Column; import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import
  javax.persistence.Id; import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import
  javax.persistence.OneToOne; import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import lombok.Data;
  
  @Entity
  @Table(name="flat_owners")
  @Data
  public class FlatOwners {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
 private long flatownersId;
  @OneToOne
  private Flats flats;
   private String firstname;
   private String lastName;
 private String email; 
  private String phone;
  private String alternatePhone;
  private Date createdAt;
	private Date updatedAt;
	private String type; 
	private boolean ownerActive;
	@OneToOne
	private FlatResidencies flatResidencies;
	@OneToOne
	private FlatDetails flatDetails;
	
	
  
  
  
  }
 