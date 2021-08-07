package com.dizitiveit.pms.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dizitiveit.pms.model.DailyVisitors;

public interface DailyVisitorsDao extends JpaRepository<DailyVisitors, Long>{
	
	DailyVisitors findByMobile(String mobile);
	DailyVisitors findById(long dailyVisitorsId);
	

}
