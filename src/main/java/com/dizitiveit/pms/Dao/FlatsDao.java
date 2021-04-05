package com.dizitiveit.pms.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.pms.model.Flats;

public interface FlatsDao extends JpaRepository<Flats, Long> {
	
	Flats findByflatId(long flatId);
	//List<Flats> findAll();
	Flats findByflatNo(String flatNo);
	 @Query(value = "select flat_no FROM pms.flats order by flat_no", nativeQuery = true)
	 List<String> findAllFlats();
	 
	 @Query(value = "select * FROM pms.flats order by flat_no", nativeQuery = true)
	 List<Flats> findAll();
}
