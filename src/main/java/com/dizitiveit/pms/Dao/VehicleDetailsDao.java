package com.dizitiveit.pms.Dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.VehicleDetails;
import com.dizitiveit.pms.pojo.VehicleDetailsPojo;

public interface VehicleDetailsDao extends JpaRepository<VehicleDetails, Long>{
	
	List<VehicleDetails> findByFlatsFlatId(long flatId);
	List<VehicleDetails> findByFlatsFlatNo(long flatNo);
	List<VehicleDetails> findByFlats(Flats flats);
	VehicleDetails findByregNo(String regNo);
	VehicleDetails findBytype(String type);
	//List<VehicleDetails> findBystatus(String status);
	VehicleDetails deleteById(long vehicleId);
	
	@Query(value = "select *  FROM vehicle_details where vehicle_id=?1", nativeQuery = true)
	 VehicleDetails findByvehicleId(long vehicleId);
	
	 @Query(value = "select *  FROM vehicle_details where reg_no=?1", nativeQuery = true)
	 VehicleDetails findByRegno(String regNo);
	 
	 @Query(value = "select * FROM pms.vehicle_details where flats_flat_id=?1 and vehicle_status=?2", nativeQuery = true)
	 List<VehicleDetails> findByflats(long flatId,boolean vehicleStatus);
	 
	 @Query(value = "select * FROM pms.vehicle_details where flats_details_flatdetails_id=?1", nativeQuery = true)
	 List<VehicleDetails> findByflatDetails(long flatdetailsId);
	 
	 @Query(value = "select * FROM pms.vehicle_details where flats_flat_id=?1 and vehicle_status=?2", nativeQuery = true)
	 List<VehicleDetails> findByflatsAndVehicleStatus(long flatId,boolean vehicleStatus);
	 
	 @Query(value = "select * FROM pms.vehicle_details where flat_owners_flatowners_id=?1 and vehicle_status=?2", nativeQuery = true)
	 List<VehicleDetails> findByflatOwners(long flatOwnersId,boolean vehicleStatus);
	 
	 @Query(value = "select * FROM pms.vehicle_details where flat_residencies_flatresidencies_id=?1 and vehicle_status=?2", nativeQuery = true)
	 List<VehicleDetails> findByflatTenants(long flatResidenciesId,boolean vehicleStatus);
	 
	
	 
	 @Modifying
     @Transactional
	 @Query(value = "delete  from vehicle_details where flats_flat_id=?1 ", nativeQuery = true)
	 int removeVehicleDetailsByflatId(long flatId);
	 
	 @Query(value = "select reg_no FROM pms.vehicle_details where vehicle_status=?1 ", nativeQuery = true)
	 List<String> findAllVehicles(boolean vehicleStatus);

}
