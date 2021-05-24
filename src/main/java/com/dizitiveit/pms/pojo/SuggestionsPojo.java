package com.dizitiveit.pms.pojo;

import java.util.Date;

import lombok.Data;
@Data
public class SuggestionsPojo {

	private long suggestinId;
	private String suggestion;
	private String createdAt;
	private boolean suggestionRead;
	private String flatNo; 
}
