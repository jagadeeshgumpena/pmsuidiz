package com.dizitiveit.pms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="visitor_parking_slots")
@Data
public class VisitorParkingSlots {
 
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long slotId;
	private String slotNo;
	private boolean slotOccupied;
	private double slotCostPerHour;
	private String vehicleType;
}
