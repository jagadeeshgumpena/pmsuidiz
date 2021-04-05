package com.dizitiveit.pms.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.pms.model.AdditionalParkingSlots;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.VisitorParkingSlots;

public interface AdditionalParkingSlotsDao  extends JpaRepository<AdditionalParkingSlots,Long> {
	
	AdditionalParkingSlots findByVehicleSlotNo(String vehicleSlotNo);
	List<AdditionalParkingSlots> findByFlats(Flats flats);
	
	 @Query(value = "select *  FROM pms.additional_parking_slots where slot_occupied=?1", nativeQuery = true)
	 List<AdditionalParkingSlots> findByslotOccupied(boolean slotOccupied);

}
