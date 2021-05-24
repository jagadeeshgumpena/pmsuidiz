package com.dizitiveit.pms.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.pms.model.BuildingSecurity;
import com.dizitiveit.pms.model.Slots;

public interface BuildingSecurityDao extends JpaRepository<BuildingSecurity,Long>  {

	BuildingSecurity findByMobile(String mobile);
	BuildingSecurity findBySecurityId(long securityId);
	List<BuildingSecurity> findAll();
	
	BuildingSecurity deleteById(long securityId);
	
	 @Query(value = "select * FROM pms.building_security where mobile=?1 and security_active=?2 order by security_id asc", nativeQuery = true)
	 BuildingSecurity getByMobile(String mobile,boolean securityActive);
	 
	 @Query(value = "select * FROM pms.building_security where security_active=?1 order by security_id asc", nativeQuery = true)
	 List<BuildingSecurity> getAllSecurities(boolean securityActive);
}
