package com.dizitiveit.pms.model;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;
	  private final String jwt;
	private final String role;
	private final String firstName;
	private final String lastName;
	private final int flatNo;
  
	  
	  public String getJwt() { 
		  return jwt; 
		  }
	  
	


	public String getRole() {
		return role;
	}




	public String getLastName() {
		return lastName;
	}


	public String getFirstName() {
		return firstName;
	}




	public int getFlatNo() {
		return flatNo;
	}



	public AuthenticationResponse(String jwt, String role, String firstName, String lastName, int flatNo) {
		super();
		this.jwt = jwt;
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.flatNo = flatNo;
	} 
	
	

}
