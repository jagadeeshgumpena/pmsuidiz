package com.dizitiveit.pms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="daily_visitors")
@Data
public class DailyVisitors {

	@Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	private long dailyVisitorId;
	private String name;
	private String category;
	private String mobile;
	
}
