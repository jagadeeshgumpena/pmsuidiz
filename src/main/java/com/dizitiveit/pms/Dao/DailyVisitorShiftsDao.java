package com.dizitiveit.pms.Dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.pms.model.DailyVisitorsShifts;
import com.dizitiveit.pms.model.SecurityShifts;

public interface DailyVisitorShiftsDao extends JpaRepository<DailyVisitorsShifts, Long>{
	
	@Query(value = "select * FROM pms.daily_visitors_shifts WHERE flats_flat_id=?1 and daily_visitors_daily_visitor_id=?2 and active=?3", nativeQuery = true)
	 DailyVisitorsShifts getByActiveService(long flatId,long dailyVisitorId,boolean active);
	
	@Query(value = "select * FROM pms.daily_visitors_shifts WHERE ?1 between start_date and end_date and active=?2", nativeQuery = true)
	List<DailyVisitorsShifts> findByDailyVisitorsToday(Date endDate,boolean activeSlot);
	
	List<DailyVisitorsShifts> findByendDateLessThan(Date endDate);

}
