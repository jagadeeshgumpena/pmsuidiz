package com.dizitiveit.pms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Data
@Entity
@Table(name="animities_fee_structure")
public class AnimitiesFeeStructure {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long animityId;
	private String type;
	private String description;
	private double amount;
	private double tax;
	private Date createdAt;
	private Date updatedAt;
}
