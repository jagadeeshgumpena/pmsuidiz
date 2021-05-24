package com.dizitiveit.pms.pojo;

import java.util.List;

import com.dizitiveit.pms.model.Flats;

import lombok.Data;

public class SlotsListPojo {

	private List<String> slotNo;
	private String floor;
	private String block;
	private String vehicleType;
	private String billingType;
	
	
	public List<String> getSlotNo() {
		return slotNo;
	}
	public void setSlotNo(List<String> slotNo) {
		this.slotNo = slotNo;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getBillingType() {
		return billingType;
	}
	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}
	
}
