package com.dizitiveit.pms.pojo;

import java.util.List;

import com.dizitiveit.pms.model.EmergencyContacts;

import lombok.Data;

@Data
public class EmergencyContactsPojo {

	private long Id;
	private String emergencyType; 
	private String firstName;
	private String lastName;
	private String mobile;
	
}
