package com.dizitiveit.pms.Dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.pms.model.BuildingSecurity;
import com.dizitiveit.pms.model.ReLoginDetails;
import com.dizitiveit.pms.model.SecurityShifts;

public interface ReLoginDetailsDao extends JpaRepository<ReLoginDetails,Long>{
	
	List<ReLoginDetails> findByBuildingSecurity(BuildingSecurity buildingSecurity);
	
	 @Query(value = "select * FROM pms.relogin_details where start_date>=?1 ", nativeQuery = true)
	 List<SecurityShifts> findByShiftStartDate(Date startDate);

	 

	 @Query(value ="select * FROM pms.relogin_details WHERE re_login_time >= CURRENT_DATE+'00:00:00' and building_security_security_id=?1",nativeQuery = true)
	 List<ReLoginDetails> findByTodayDate(long securityId);
	 
	 @Modifying
	  @Transactional
	  @Query(value = "delete  from relogin_details where building_security_security_id=?1 ", nativeQuery = true)
	  int removeReLoginDetailsBysecurityId(long securityId);
}
