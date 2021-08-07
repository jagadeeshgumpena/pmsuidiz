package com.dizitiveit.pms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="daily_visitors_shifts")
@Data
public class DailyVisitorsShifts {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long dailyVisitorShiftsId;
	@ManyToOne
	private DailyVisitors dailyVisitors;
	private String shiftTime;
	@ManyToOne
	private Flats flats;
    private Date startDate;
    private Date endDate;
    private Date createdAt;
    private boolean active;

}
