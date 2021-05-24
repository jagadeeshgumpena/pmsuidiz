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
@Table(name="suggestions")
@Data
public class Suggestions {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long suggestinId;
	private String suggestion;
	private Date createdAt;
	private boolean suggestionRead;
	@ManyToOne
	private Flats flats;
}
