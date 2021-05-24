package com.dizitiveit.pms.Dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dizitiveit.pms.model.BuildingSecurity;
import com.dizitiveit.pms.model.SecurityShifts;
import com.dizitiveit.pms.model.VehicleDetails;

public interface SecurityShiftsDao extends JpaRepository<SecurityShifts, Long> {
	
	List<SecurityShifts> findByBuildingSecurity(BuildingSecurity buildingSecurity);
	
	@Query(value = "select * FROM pms.security_shift where shift_id=?1", nativeQuery = true)
	 SecurityShifts findByShiftId(long shiftId);
	
	 @Query(value = "select * FROM pms.security_shift where shift_slot=?1 and  active_slot=?2", nativeQuery = true)
	 List<SecurityShifts> findByShiftSlot(String shiftSlot,boolean activeSlot);

	 @Query(value = "select * FROM pms.security_shift where start_date>=?1 and end_date<=?2", nativeQuery = true)
	 List<SecurityShifts> findByShiftSlotWithDate(Date startDate,Date endDDate);
	 
	 @Query(value = "select * FROM pms.security_shift where building_security_security_id=?1  and  active_slot=?2", nativeQuery = true)
	 List<SecurityShifts> findByShiftSlotBySecurityId(long securityId,boolean activeSlot);
	 
	 @Query(value = "select * FROM pms.security_shift where building_security_security_id=?1 and start_date>=?2 and end_date<=?3", nativeQuery = true)
	 SecurityShifts findByLoginDate(long securityId,Date startDate,Date endDate);
	 
	 @Query(value = "select * FROM pms.security_shift where start_date>=?1 ", nativeQuery = true)
	 List<SecurityShifts> findByShiftStartDate(Date startDate);
	 
	 List<SecurityShifts> findByendDateLessThan(Date endDate);
	 
	 List<SecurityShifts> findByendDateGreaterThan(Date endDate);
	 
	 public List<SecurityShifts> findAllByendDateBetween(Date fromDate,Date toDate);
	 
	 @Query(value = "select * FROM pms.security_shift where building_security_security_id=?1 and end_date>?2", nativeQuery = true)
	 List<SecurityShifts> findBySecurityAfterEndDate(long securityId,Date endDate);
	 
	 
	 @Query(value = "select * FROM pms.security_shift WHERE ?1 between start_date and end_date and active_slot=?2", nativeQuery = true)
	 List<SecurityShifts> findBySecurityToday(Date endDate,boolean activeSlot);
	 
	 @Modifying
	  @Transactional
	  @Query(value = "delete  from security_shift where building_security_security_id=?1 ", nativeQuery = true)
	  int removeSecurityShitsBysecurityId(long securityId);
	 
	 
}
