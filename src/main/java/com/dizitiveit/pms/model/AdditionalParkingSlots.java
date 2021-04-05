package com.dizitiveit.pms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
@Entity
@Table(name="additional_parking_slots")
@Data
public class AdditionalParkingSlots {
	 @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private long additionalVehicleSlotId;
	  private String vehicleSlotNo;
	  private boolean slotOccupied;
	  private String vehicleType;
	  @ManyToOne
	  private Flats flats;
	 
	 

}
