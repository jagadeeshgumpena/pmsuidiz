package com.dizitiveit.pms.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.pms.model.FlatDetails;
import com.dizitiveit.pms.model.FlatOwners;
import com.dizitiveit.pms.model.FlatResidencies;
import com.dizitiveit.pms.model.Flats;

public interface FlatOwnersDao extends JpaRepository<FlatOwners,Long> {

	FlatOwners findByFlats(Flats flats);
	FlatOwners findByEmail(String email);
	FlatOwners findByFlatResidencies(FlatResidencies flatResidencies);
	
	FlatOwners findByPhone(String phone);
	
	 @Query(value = "select *  FROM flat_owners where flats_flat_id=?1 and owner_active=?2", nativeQuery = true)
	 FlatOwners findByownersActive(long flatId,boolean ownerActive);
	 
	 @Query(value = "select *  FROM flat_owners where flats_flat_id=?1 and owner_active=?2 and type=?3", nativeQuery = true)
	 FlatOwners findByownersActiveAndType(long flatId,boolean ownerActive,String Type);
	 
	 @Query(value = "select *  FROM flat_owners where phone=?1 and owner_active=?2", nativeQuery = true)
	 FlatOwners findByownersPhone(String phone,boolean ownerActive);
	 
	 @Query(value = "select *  FROM flat_owners where flats_details_flatdetails_id=?1", nativeQuery = true)
	 FlatOwners findByownersDeactivate(long flatDetails );
	 
	 @Query(value = "select *  FROM flat_owners where owner_active=?1 and type=?2", nativeQuery = true)
	 List<FlatOwners> findByownersDeactiveAndType(boolean ownerActive,String type);
	 
	 
}
