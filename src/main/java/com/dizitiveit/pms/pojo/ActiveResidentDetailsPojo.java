package com.dizitiveit.pms.pojo;

import java.util.List;

import lombok.Data;
@Data
public class ActiveResidentDetailsPojo {

	private FlatOwnersPojo resident;
	private List<SlotsPojo> slots;
	private List<VehicleDetailsPojo> vehicleDetails;
	
}
