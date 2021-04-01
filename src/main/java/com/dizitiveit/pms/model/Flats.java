
  package com.dizitiveit.pms.model;
  
  import java.util.Date;

import javax.persistence.Column; 
  import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id; 
  import javax.persistence.Table;
  import lombok.Data; 
  import lombok.Getter; 
  import lombok.Setter;
  
  
@Data
@Entity
 @Table(name="flats")
  public class Flats {

	@Id	
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long flatId; 
  private int flatNo; 
  private String block; 
  private String tower; 
  private int floor; 
  private Date createdAt;
	private Date updatedAt;
  }
 