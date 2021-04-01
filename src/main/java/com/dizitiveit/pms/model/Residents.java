package com.dizitiveit.pms.model;

import java.util.List;

import com.dizitiveit.pms.pojo.FlatDetailsPojo;
import com.dizitiveit.pms.pojo.FlatOwnersPojo;
import com.dizitiveit.pms.pojo.FlatResidenciesPojo;
import com.dizitiveit.pms.pojo.VehicleDetailsPojo;

public class Residents {

	private FlatOwnersPojo flatOwners;
	private FlatResidenciesPojo flatResidencies;
	private FlatDetailsPojo flatDetails;
	private List<VehicleDetailsPojo> vehicleDetails;
	
	
	public FlatDetailsPojo getFlatDetails() {
		return flatDetails;
	}
	public void setFlatDetails(FlatDetailsPojo flatDetails) {
		this.flatDetails = flatDetails;
	}
	public FlatResidenciesPojo getFlatResidencies() {
		return flatResidencies;
	}
	public void setFlatResidencies(FlatResidenciesPojo flatResidencies) {
		this.flatResidencies = flatResidencies;
	}
	public FlatOwnersPojo getFlatOwners() {
		return flatOwners;
	}
	public void setFlatOwners(FlatOwnersPojo flatOwners) {
		this.flatOwners = flatOwners;
	}
	public List<VehicleDetailsPojo> getVehicleDetails() {
		return vehicleDetails;
	}
	public void setVehicleDetails(List<VehicleDetailsPojo> vehicleDetails) {
		this.vehicleDetails = vehicleDetails;
	}
	
	
	
		
	
	
	
}
