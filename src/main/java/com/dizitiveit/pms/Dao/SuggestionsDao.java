package com.dizitiveit.pms.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.Slots;
import com.dizitiveit.pms.model.Suggestions;

public interface SuggestionsDao extends JpaRepository<Suggestions,Long> {

	List<Suggestions> findByFlats(Flats flats);
	
	 @Query(value = "select * FROM pms.suggestions where suggestin_id=?1 order by suggestin_id desc", nativeQuery = true)
	 Suggestions findBysuggestinId(long suggestinId);
	 
	 @Query(value = "select * FROM pms.suggestions where flats_flat_id=?1 order by suggestin_id desc", nativeQuery = true)
	 List<Suggestions> getFlats(long flatId);
}
