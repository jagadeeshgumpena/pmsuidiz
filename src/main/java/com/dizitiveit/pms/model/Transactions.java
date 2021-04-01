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
@Table(name="transactions")
@Data
public class Transactions {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long txnId;
	private String paymentId;
	private double amount;
	private String status;
	private String currency;
	@ManyToOne
	private Flats flats;
	@ManyToOne
	private Users users;
	private Date createdAt;
	private Date updatedAt;
	
	
	

}
