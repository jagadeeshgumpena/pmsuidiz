package com.dizitiveit.pms.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dizitiveit.pms.model.FlatDetails;
import com.dizitiveit.pms.model.FlatOwners;
import com.dizitiveit.pms.model.Flats;

public interface FlatDetailsDao extends JpaRepository<FlatDetails,Long> {
	
	FlatDetails findByflatdetailsId(long flatDetailsId);
	
	FlatDetails findByFlats(Flats flats);

}
