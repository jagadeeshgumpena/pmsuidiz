package com.dizitiveit.pms.pojo;

import java.util.HashMap;
import java.util.List;

import com.dizitiveit.pms.model.FlatDetails;
import com.dizitiveit.pms.model.FlatResidencies;
import com.dizitiveit.pms.model.VehicleDetails;

public class ResidenciesFlatDetails {

	private FlatDetails flatDetails;
	private FlatResidencies flatResidencies;
	//private List<VehicleDetailsPojo> vehicleDetails;
	public FlatDetails getFlatDetails() {
		return flatDetails;
	}
	public void setFlatDetails(FlatDetails flatDetails) {
		this.flatDetails = flatDetails;
	}
	public FlatResidencies getFlatResidencies() {
		return flatResidencies;
	}
	public void setFlatResidencies(FlatResidencies flatResidencies) {
		this.flatResidencies = flatResidencies;
	}
	
	
	
	
	
}
