package com.dizitiveit.pms.model;

import java.io.Serializable;

public class SecurityAuthenticationResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	  private  String jwt;
	private  String role;
	private  String firstName;
	private  String lastName;
    private String startDate;
    private String startTime;
	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public SecurityAuthenticationResponse(String jwt, String role, String firstName, String lastName, String startDate,
			String startTime) {
		super();
		this.jwt = jwt;
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.startDate = startDate;
		this.startTime = startTime;
	}
    
    
}
