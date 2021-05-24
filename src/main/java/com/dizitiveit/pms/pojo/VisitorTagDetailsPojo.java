package com.dizitiveit.pms.pojo;

import java.util.Date;
import java.util.List;

import javax.persistence.ManyToOne;

import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.VisitorParkingSlots;
import com.dizitiveit.pms.model.VisitorTagDetails;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Data
public class VisitorTagDetailsPojo {
 
	private long visitorId;
	private String visitorName;
	private String phoneNumber;
	private String blockNumber;
	private String flatNo;
	private String vehicleType;
	private String brand;
	private String model;
	private String inTime;
	private String outTime;
	private String expectedInTime;
	private String expectedOutTime;
	private String createdAt;
	private String  vehicleNumber;
	private boolean status; 
	private String type;
	private String purpose;
	private String visitorStatus;
	private double parkingCost;
	private String parkingSLot;
	private String residentType;
	private String slotNo;
	
}
