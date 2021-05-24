package com.dizitiveit.pms.Dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.pms.model.BuildingSecurity;
import com.dizitiveit.pms.model.FlatOwners;
import com.dizitiveit.pms.model.GeneralInvoice;
import com.dizitiveit.pms.model.VisitorParkingSlots;

public interface GeneralInvoiceDao extends JpaRepository<GeneralInvoice,Long>{

	
	 GeneralInvoice findBycreatedAt(Date createdAt);
	 
	 @Query(value = "SELECT * FROM pms.general_invoice WHERE MONTH(created_at) =?1  and YEAR(created_at) =?2", nativeQuery = true)
	 GeneralInvoice findByCreatedAt(long month,long year);
	 
	 @Query(value = "SELECT * FROM pms.general_invoice order by created_at desc limit 6", nativeQuery = true)
	 List<GeneralInvoice> findByLatest();
	 
}
