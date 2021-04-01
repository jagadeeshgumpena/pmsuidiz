package com.dizitiveit.pms.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dizitiveit.pms.model.EmailLink;

public interface EmailLinkDao extends JpaRepository<EmailLink, Integer> {
	
	EmailLink findByvalue(String value);

}
