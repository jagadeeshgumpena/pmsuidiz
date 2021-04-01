package com.dizitiveit.pms.Dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.pms.model.Invoice;

public interface InvoiceDao extends JpaRepository<Invoice,Long> {

	List<Invoice> findByFlatsFlatNo(int flatNo);
	//List<Invoice> findByInvoicecreatedAt(Date createdAt);
 
	
	 @Query(value = "select * from invoice where MONTH(curr_date)=?1 AND flats_flat_id=?2", nativeQuery = true)
	 List<Invoice> FindLatestInvoice(int month,long flatId);
	 
	@Query(value="select * from invoice WHERE curr_date >=DATE_ADD(NOW(), INTERVAL -6 MONTH) AND flats_flat_id=?1",nativeQuery = true)
	List<Invoice> GetLastSixMonthsInvoices(long flatId);
	
	@Query(value="select * from invoice WHERE MONTH(curr_date)=?1 AND YEAR(curr_date)=?2 AND flats_flat_id=?3",nativeQuery = true)
	List<Invoice> getInvoicesByMonthAndYear(int month,int year, long flatId);
}
