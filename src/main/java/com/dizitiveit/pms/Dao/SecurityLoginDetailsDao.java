package com.dizitiveit.pms.Dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.pms.model.BuildingSecurity;
import com.dizitiveit.pms.model.ReLoginDetails;
import com.dizitiveit.pms.model.SecurityLoginDetails;
import com.dizitiveit.pms.model.SecurityShifts;

public interface SecurityLoginDetailsDao extends JpaRepository<SecurityLoginDetails, Long>  {

	List<SecurityLoginDetails> findByBuildingSecurity(BuildingSecurity buildingSecurity);

	
	 @Query(value = "select * FROM pms.security_login_details where building_security_security_id=?1  and  purpose=?2", nativeQuery = true)
	 SecurityLoginDetails findByShiftSlotBySecurityId(long securityId,String purpose);
	 
	 @Query(value ="select * FROM pms.security_login_details WHERE in_time >= CURRENT_DATE+'00:00:00' and building_security_security_id=?1",nativeQuery = true)
	 List<SecurityLoginDetails> findByTodayDate(long securityId);
	 
	 @Query(value = "select * FROM pms.security_login_details where purpose=?1", nativeQuery = true)
	 List<SecurityLoginDetails> findByPurpose(String purpose);
	 
	 @Modifying
	  @Transactional
	  @Query(value = "delete  from security_login_details where building_security_security_id=?1 ", nativeQuery = true)
	  int removeSecurityLoginDetailsBysecurityId(long securityId);
}
