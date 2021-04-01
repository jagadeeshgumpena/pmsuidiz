package com.dizitiveit.pms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.Data;
@Entity
@Table(name="security_login_details")
@Data
public class SecurityLoginDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long securityLoginId;
	private Date inTime;
	private Date outTime;
	private String purpose;
	private Date reLogin;
	@ManyToOne
	private BuildingSecurity buildingSecurity;

}
