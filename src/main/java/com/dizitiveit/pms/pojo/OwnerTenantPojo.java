package com.dizitiveit.pms.pojo;

import java.util.List;

import com.dizitiveit.pms.model.FlatOwners;
import com.dizitiveit.pms.model.FlatResidencies;
import com.dizitiveit.pms.model.VehicleDetails;

public class OwnerTenantPojo {
private FlatOwners flatOwners;
private List<VehicleDetails> vehiclesDetails;
private FlatResidencies flatResidencies;
public FlatOwners getFlatOwners() {
	return flatOwners;
}
public void setFlatOwners(FlatOwners flatOwners) {
	this.flatOwners = flatOwners;
}
public List<VehicleDetails> getVehiclesDetails() {
	return vehiclesDetails;
}
public void setVehiclesDetails(List<VehicleDetails> vehiclesDetails) {
	this.vehiclesDetails = vehiclesDetails;
}
public FlatResidencies getFlatResidencies() {
	return flatResidencies;
}
public void setFlatResidencies(FlatResidencies flatResidencies) {
	this.flatResidencies = flatResidencies;
}


}
