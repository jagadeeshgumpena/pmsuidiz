package com.dizitiveit.pms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="relogin_Details")
@Data
public class ReLoginDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long reLoginId;
	private Date reLoginTime;
	@ManyToOne
	private BuildingSecurity buildingSecurity;
	
}
