package com.dizitiveit.pms.pojo;

import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dizitiveit.pms.model.Slots;
import com.dizitiveit.pms.model.VehicleDetails;

import lombok.Data;

@Data
public class VehicleMovementRegisterPojo {
	private long vehiclemovementId;
	private String regNo;
	private String vehicleIn;
	private String vehicleOut;
	private long vehicleId;
	private String type;
	private String color;
	private String make;
	private String model;
	private boolean vehicleStatus;
	private String slotNo;
	private String flatNo;
	private double parkingCost;
}
