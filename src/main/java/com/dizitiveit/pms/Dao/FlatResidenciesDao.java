package com.dizitiveit.pms.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.pms.model.FlatOwners;
import com.dizitiveit.pms.model.FlatResidencies;
import com.dizitiveit.pms.model.Flats;

public interface FlatResidenciesDao extends JpaRepository<FlatResidencies,Long> {

	FlatResidencies save(FlatResidencies flatResidencies);

	FlatResidencies findByPhone(String phone);
	
	FlatResidencies findByFlats(Flats flats);
	
	
	 @Query(value = "select *  FROM flat_residencies where flats_flat_id=?1 and tenant_active=?2", nativeQuery = true)
	 FlatResidencies findBytenantsActive(long flatId,boolean tenantActive);
	 
	 @Query(value = "select *  FROM flat_residencies where phone=?1 and tenant_active=?2", nativeQuery = true)
	 FlatResidencies findBytenantsPhone(String phone,boolean tenantActive);
	 
	 @Query(value = "select *  FROM flat_residencies where tenant_active=?1", nativeQuery = true)
	 List<FlatResidencies> findBytenantsDeactive(boolean tenantActive);

}
