package com.dizitiveit.pms.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dizitiveit.pms.model.Responses;



public interface ResponsesDao extends JpaRepository<Responses,Integer>{
	
	Responses findById(int responsesId);

}
