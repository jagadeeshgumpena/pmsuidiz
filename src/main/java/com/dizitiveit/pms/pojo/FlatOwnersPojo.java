package com.dizitiveit.pms.pojo;

import java.util.Date;

import javax.persistence.OneToOne;

import com.dizitiveit.pms.model.Flats;

public class FlatOwnersPojo {

	

	  private String flatNo;
	   private String firstname;
	   private String lastName;
	 private String email; 
	  private String phone;
	  private String type;
	  private String createdAt;
	  private String deactivateddAt;	
		
		public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
		public String getFirstname() {
			return firstname;
		}
		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		
		public String getFlatNo() {
			return flatNo;
		}
		public void setFlatNo(String flatNo) {
			this.flatNo = flatNo;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getDeactivateddAt() {
			return deactivateddAt;
		}
		public void setDeactivateddAt(String deactivateddAt) {
			this.deactivateddAt = deactivateddAt;
		}
		
		
		
		
}
