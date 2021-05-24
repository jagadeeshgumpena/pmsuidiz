package com.dizitiveit.pms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table(name="vehicle_movement_register")
@Data
public class VehicleMovementRegister {
	
	@Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	private long vehiclemovementId;
	@ManyToOne
	private VehicleDetails vehicleDetails;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "Asia/Kolkata")
	private Date vehicleIn;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "Asia/Kolkata")
	private Date vehicleOut;
	private Date createdAt;
	private Date updatedAt;
	@ManyToOne
	private Slots slots;
	private double parkingCost;

}
