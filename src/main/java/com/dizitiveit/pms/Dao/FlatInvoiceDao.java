package com.dizitiveit.pms.Dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.pms.model.FlatInvoice;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.GeneralInvoice;
import com.dizitiveit.pms.model.Invoice;

public interface FlatInvoiceDao extends JpaRepository<FlatInvoice,Long> {
	

	 @Query(value = "select *  FROM flat_invoice where flats_flat_id=?1 and created_at ", nativeQuery = true)
	 FlatInvoice findByFlats(long flatId,Date createdAt);
	 
	 FlatInvoice findByFlats(Flats flats);
	 
		
		  @Query(value = "select *  FROM flat_invoice where flats_flat_id=?1 ", nativeQuery = true) 
		 List<FlatInvoice> getByFlats(long flatId);
		  
		  @Query(value = "SELECT * FROM pms.flat_invoice WHERE MONTH(created_at) =?1  and YEAR(created_at) =?2 and flats_flat_id=?3", nativeQuery = true)
		  FlatInvoice getInvoice(long month,long year,long flatId);
		  
		  @Query(value = "SELECT * FROM pms.flat_invoice WHERE flats_flat_id=?1 order by created_at desc limit 1", nativeQuery = true)
		  FlatInvoice getInvoiceByFlat(long flatId);
		  
		  
		 
	
}
