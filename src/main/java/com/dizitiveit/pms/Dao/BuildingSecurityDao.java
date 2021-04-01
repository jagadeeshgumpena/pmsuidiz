package com.dizitiveit.pms.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dizitiveit.pms.model.BuildingSecurity;

public interface BuildingSecurityDao extends JpaRepository<BuildingSecurity,Long>  {

	BuildingSecurity findByMobile(String mobile);
	BuildingSecurity findBySecurityId(long securityId);
	List<BuildingSecurity> findAll();
	
	BuildingSecurity deleteById(long securityId);
}
