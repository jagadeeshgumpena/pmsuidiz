package com.dizitiveit.pms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="responses")
public class Responses {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="responses_id")
    private int responsesId;
	 
	@Column(name="res_name")
	private String resName;
	 
	 
	
	public Responses() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Responses(int responsesId, String resName) {
		this.responsesId=responsesId;
		this.resName=resName;
	}

	public int getResponsesId() {
		return responsesId;
	}

	public void setResponsesId(int responsesId) {
		this.responsesId = responsesId;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}
	
	
	
	
}
