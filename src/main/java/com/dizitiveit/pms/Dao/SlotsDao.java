package com.dizitiveit.pms.Dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.Slots;
import com.dizitiveit.pms.model.VisitorTagDetails;

public interface SlotsDao extends JpaRepository<Slots,Long> {
 Slots findByslotNo(String slotNo);
 Slots findByFlats(Flats flats);
 List<Slots> findByBillingType(String billingType);
 
  @Query(value = "select * FROM pms.slots where assigned=?1 order by slot_no asc", nativeQuery = true)
 List<Slots> findBySlots(boolean assigned);
 
 @Query(value = "select * FROM pms.slots where assigned=?1 and floor=?2 order by slot_no asc", nativeQuery = true)
 List<Slots> findBySlotsInFloor(boolean assigned,String floor);
 
 @Query(value = "select * FROM pms.slots  order by slot_no asc", nativeQuery = true)
 List<String> findBySlots();
 
 @Query(value = "select * FROM pms.slots where flats_flat_id=?1 order by slot_no asc", nativeQuery = true)
 List<Slots> findByflatId(long flatId);
	
 @Query(value = "select * FROM pms.slots where slot_no=?1 and floor=?2 order by slot_no asc", nativeQuery = true)
 Slots findByslotsNo(String slotNo,String floor);	
 
 @Query(value = "select * FROM pms.slots where slot_no=?1 order by slot_no asc", nativeQuery = true)
 Slots getslotsNoByType(String slotNo);	
 
 @Query(value = "select * FROM pms.slots where flats_flat_id=?1 and vehicle_type=?2 and is_occupied=?3 order by slot_no asc limit 1", nativeQuery = true)
 List<Slots> getByvehicleType(long flatId,String vehicleType,boolean isOcuupied);	
 
 @Query(value = "select * FROM pms.slots where billing_type=?1 and is_occupied=?2  order by slot_no asc", nativeQuery = true)
 List<Slots> findbyBillingType(String billingType,boolean occupied);
 
 @Query(value = "select * FROM pms.slots where billing_type=?1 and filled=?2  order by slot_no asc", nativeQuery = true)
 List<Slots> getByBillingTyie(String billingType,boolean filled);
 
 @Query(value="select * from pms.slots where flats_flat_id=?1  and billing_type=?2 order by slot_no asc",nativeQuery = true)
	List<Slots> getByflatNo(long flatId,String billingType);
 
 @Query(value="select * from pms.slots where slot_no=?1 order by slot_no asc",nativeQuery = true)
	List<Slots> getByslotNo(String slotNo);
}
