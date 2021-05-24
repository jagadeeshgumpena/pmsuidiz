package com.dizitiveit.pms.pojo;

import java.util.HashMap;
import java.util.List;

import com.dizitiveit.pms.model.FlatDetails;
import com.dizitiveit.pms.model.FlatOwners;
import com.dizitiveit.pms.model.FlatResidencies;
import com.dizitiveit.pms.model.Slots;
import com.dizitiveit.pms.model.VehicleDetails;

public class ResidenciesFlatDetails {

	
	private List<Slots> slots;
	private FlatResidencies flatResidencies;
	//private List<VehicleDetailsPojo> vehicleDetails;
	private List<VehicleDetails> vehicleDetails;
	
	public FlatResidencies getFlatResidencies() {
		return flatResidencies;
	}
	public List<Slots> getSlots() {
		return slots;
	}
	public void setSlots(List<Slots> slots) {
		this.slots = slots;
	}
	public void setFlatResidencies(FlatResidencies flatResidencies) {
		this.flatResidencies = flatResidencies;
	}
	
}
