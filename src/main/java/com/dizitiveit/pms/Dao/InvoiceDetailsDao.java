package com.dizitiveit.pms.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.pms.model.FlatInvoice;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.GeneralInvoice;
import com.dizitiveit.pms.model.Invoice;
import com.dizitiveit.pms.model.InvoiceDetails;

public interface InvoiceDetailsDao extends JpaRepository<InvoiceDetails,Long>{

 InvoiceDetails findByFlats(Flats flats);
 
 @Query(value = "SELECT * FROM pms.invoice_details WHERE MONTH(created_at) =?1  and YEAR(created_at) =?2", nativeQuery = true)
 InvoiceDetails findByCreatedAt(long month,long year);
 
 @Query(value = "SELECT * FROM pms.invoice_details WHERE MONTH(created_at) =?1  and YEAR(created_at) =?2 and flats_flat_id=?3", nativeQuery = true)
 InvoiceDetails getInvoiceDetails(long month,long year,long flatId);
 
 @Query(value = "SELECT * FROM pms.invoice_details WHERE flats_flat_id=?1 and MONTH(created_at) =?2  and YEAR(created_at) =?3 order by created_at desc limit 1", nativeQuery = true)
 InvoiceDetails getInvoiceByFlat(long flatId,long month,long year);
 
 @Query(value = "SELECT * FROM pms.invoice_details WHERE flats_flat_id=?1 order by created_at desc limit 1", nativeQuery = true)
 List<InvoiceDetails> InvoiceByFlat(long flatId);
 
 @Query(value = "SELECT * FROM pms.invoice_details WHERE flats_flat_id=?1 order by created_at desc limit 1", nativeQuery = true)
 InvoiceDetails getLatestInvoiceByFlat(long flatId);
	
}
