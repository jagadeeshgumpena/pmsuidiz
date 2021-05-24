package com.dizitiveit.pms.pojo;

import java.util.List;

import com.dizitiveit.pms.model.FlatOwners;
import com.dizitiveit.pms.model.FlatResidencies;
import com.dizitiveit.pms.model.Slots;
import com.dizitiveit.pms.model.VehicleDetails;

public class OwnerFlatDetails {

	private List<Slots> slots;
	private  FlatOwners flatOwners;
	private List<VehicleDetails> vehicleDetails;
	
	
	
	public List<VehicleDetails> getVehicleDetails() {
		return vehicleDetails;
	}
	public void setVehicleDetails(List<VehicleDetails> vehicleDetails) {
		this.vehicleDetails = vehicleDetails;
	}
	public List<Slots> getSlots() {
		return slots;
	}
	public void setSlots(List<Slots> slots) {
		this.slots = slots;
	}
	public FlatOwners getFlatOwners() {
		return flatOwners;
	}
	public void setFlatOwners(FlatOwners flatOwners) {
		this.flatOwners = flatOwners;
	}
	
	
}
