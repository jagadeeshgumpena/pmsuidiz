package com.dizitiveit.pms.pojo;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.dizitiveit.pms.model.DailyVisitors;
import com.dizitiveit.pms.model.Flats;

import lombok.Data;

@Data
public class DailyVisitorsPojo {

	private long dailyVisitorId;
	private String name;
	private String category;
	private String mobile;
	private boolean assigned;
	private String startDate;
	private String endDate;
	private String shift;
	private String flatNo;
}
