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
@Data
@Entity
@Table(name="visitor_tag_number")
public class VisitorTagDetails {
	
	@Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	private long visitorId;
	private Date createdAt;
	private String visitorName;
	private String phoneNumber;
	private String blockNumber;
	@ManyToOne
	private Flats flats;
	private String vehicleType;
	private String brand;
	private String model;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "Asia/Kolkata")
	private Date inTime;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "Asia/Kolkata")
	private Date outTime;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "Asia/Kolkata")
	private Date expectedInTime;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "Asia/Kolkata")
	private Date expectedOutTime;
	private String  vehicleNumber;
	private boolean status; 
	private String type;
	private String purpose;
	private String visitorStatus;
	private double parkingCost;
	/*
	 * private boolean monday; private boolean tuesday; private boolean wednesday;
	 * private boolean thursday; private boolean friday; private boolean saturday;
	 */
	@ManyToOne
	private VisitorParkingSlots visitorParkingSlots;
	private String residentType;
}
