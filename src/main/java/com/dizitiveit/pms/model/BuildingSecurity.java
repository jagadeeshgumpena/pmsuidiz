package com.dizitiveit.pms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="building_security")
@Data
public class BuildingSecurity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	 private long securityId;
	 private String mobile;
	 private String email;
	 private String firstName;
	 private String lastName;
	 private String address;
	 private String nationalCard;
	
	
}
