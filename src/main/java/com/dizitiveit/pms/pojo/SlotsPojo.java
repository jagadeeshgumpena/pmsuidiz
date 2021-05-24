package com.dizitiveit.pms.pojo;

import lombok.Data;

@Data
public class SlotsPojo {

	private String slotNo;
	private String floor;
	private String block;
	private String flatNo;
	private boolean isOccupied;
	private String vehicleType;
	private String billingType;
	private boolean filled;
	private boolean assigned;
}
