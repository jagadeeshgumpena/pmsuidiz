package com.dizitiveit.pms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="emergency_contacts")
@Data
public class EmergencyContacts {

	 @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	private long emergencyId;
	private String emergencyType; 
	private String firstName;
	private String lastName;
	private String mobile;
	private boolean emergencyStatus;
	private Date createdAt;
}
