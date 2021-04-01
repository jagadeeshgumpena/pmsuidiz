package com.dizitiveit.pms.model;

import java.util.List;

import lombok.Data;
@Data
public class Request {

	List<String> token;
	private String title;
    private String message;
    private String topic;
}
