package com.dizitiveit.pms.Dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.pms.model.Slots;
import com.dizitiveit.pms.model.VehicleDetails;
import com.dizitiveit.pms.model.VehicleMovementRegister;
import com.dizitiveit.pms.model.VisitorTagDetails;

public interface VehicleMovementRegisterDao extends JpaRepository<VehicleMovementRegister,Long> {

VehicleMovementRegister findByVehicleDetails(VehicleDetails vehicleDetails);
VehicleMovementRegister findById(long vehicleMovementRegisterId);

	@Query(value="select * from pms.vehicle_movement_register where  slots_slots_id=?1",nativeQuery = true)
		List<VehicleMovementRegister> findByVehicleDetails(long slotsId);
	
	@Query(value="select * from pms.vehicle_movement_register where vehicle_in is not null and vehicle_out is null;",nativeQuery = true)
	List<VehicleMovementRegister> findByvehicleIn();
	
	@Query(value="select * from pms.vehicle_movement_register where vehicle_details_vehicle_id=?;",nativeQuery = true)
	List<VehicleMovementRegister> findByvehicleMovement(long VehicleId);
	
	@Query(value="select * from pms.vehicle_movement_register where vehicle_details_vehicle_id=? and vehicle_in is not null and vehicle_out is  null;",nativeQuery = true)
	List<VehicleMovementRegister> findByvehicle(long VehicleId);
	 
	
	@Query(value = "SELECT * FROM pms.vehicle_movement_register WHERE MONTH(vehicle_out) =?1  and YEAR(vehicle_out) =?2 and vehicle_out is not null", nativeQuery = true)
	List<VehicleMovementRegister> getVehicleMovementInvoice(long month,long year);
	
	 
}
