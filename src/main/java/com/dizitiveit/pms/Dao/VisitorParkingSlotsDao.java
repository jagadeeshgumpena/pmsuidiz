package com.dizitiveit.pms.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.pms.model.FlatOwners;
import com.dizitiveit.pms.model.VisitorParkingSlots;
import java.util.List;

public interface VisitorParkingSlotsDao extends JpaRepository<VisitorParkingSlots,Long>  {

	VisitorParkingSlots findBySlotNo(String slotNo);
	
	 @Query(value = "select *  FROM pms.visitor_parking_slots where slot_occupied=?1", nativeQuery = true)
	 List<VisitorParkingSlots> findByslotOccupied(boolean slotOccupied);
	 
	
	
}
