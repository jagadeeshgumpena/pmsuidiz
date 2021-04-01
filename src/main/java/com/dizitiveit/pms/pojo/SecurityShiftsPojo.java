package com.dizitiveit.pms.pojo;

import java.time.LocalTime;
import java.util.Date;

import lombok.Data;
@Data
public class SecurityShiftsPojo {

	private long securityId;
	private Date endDate;
	private Date startDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private String shiftSlot;
	private boolean activeSlot;
}
