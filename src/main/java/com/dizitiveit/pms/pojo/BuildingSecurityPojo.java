package com.dizitiveit.pms.pojo;

import java.util.List;

import com.dizitiveit.pms.model.BuildingSecurity;

import lombok.Data;
@Data
public class BuildingSecurityPojo {
	
	/*
	 * private long securityId; private String mobile; private String email; private
	 * String firstName; private String lastName; private String address; private
	 * String nationalCard;
	 */
	 
	 BuildingSecurity buildingSecurity;
    
	 List<SecurityShiftsPojo>  securityShifts;
		List<SecurityLoginDetailsPojo> securityLoginDetails;
		List<ReLoginDetailsPojo> reLoginDetails;
}
