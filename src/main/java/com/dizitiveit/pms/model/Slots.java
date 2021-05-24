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
@Table(name="slots")
@Data
public class Slots {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long slotsId;
	private String slotNo;
	@ManyToOne
	private Flats flats;
	private String floor;
	private String block;
	private boolean isOccupied;
	private String vehicleType;
	private String billingType;
	private Date createdAt;
	private boolean assigned;
	private boolean filled;
	
}
