package com.dizitiveit.pms.model;

import java.util.HashMap;
import java.util.List;

import com.dizitiveit.pms.pojo.VehicleDetailsPojo;

public class ResidentsDetails {
 

	private FlatOwners flatOwners;
	private FlatResidencies flatTenants;
	private List<VehicleDetailsPojo> vehicleDetails;
	//private HashMap<String, List<VehicleDetailsPojo>> vehicleDetails;
	
	
	public FlatOwners getFlatOwners() {
		return flatOwners;
	}
	public List<VehicleDetailsPojo> getVehicleDetails() {
		return vehicleDetails;
	}
	public void setVehicleDetails(List<VehicleDetailsPojo> vehicleDetails) {
		this.vehicleDetails = vehicleDetails;
	}
	public void setFlatOwners(FlatOwners flatOwners) {
		this.flatOwners = flatOwners;
	}
	public FlatResidencies getFlatTenants() {
		return flatTenants;
	}
	public void setFlatTenants(FlatResidencies flatTenants) {
		this.flatTenants = flatTenants;
	}
	
	
	
	
	
	
}
