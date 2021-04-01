package com.dizitiveit.pms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="vehicle_movement_register")
public class VehicleMovementRegister {
	
	@Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	private long vehiclemovementId;
	@ManyToOne
	private VehicleDetails vehicleDetails;
	private String type;
	private Date dateTime;
	private Date createdAt;
	private Date updatedAt;
	

}
