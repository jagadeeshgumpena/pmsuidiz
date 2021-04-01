package com.dizitiveit.pms.model;

import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Table(name="security_shift")
@Entity
@Data
public class SecurityShifts {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long shiftId;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date startDate;
	@Temporal(TemporalType.DATE)
	private Date endDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private String shiftSlot;
	private boolean activeSlot;
	@ManyToOne
	private BuildingSecurity buildingSecurity;
	
	
}
