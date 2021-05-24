package com.dizitiveit.pms.Dao;

import java.util.Date;
import java.util.List;

import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.pms.model.FlatInvoice;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.Invoice;
import com.dizitiveit.pms.model.SecurityShifts;
import com.dizitiveit.pms.model.VehicleDetails;
import com.dizitiveit.pms.model.VisitorTagDetails;

public interface VisitorTagDetailsDao extends JpaRepository<VisitorTagDetails,Long> {

	List<VisitorTagDetails> findByVisitorStatus(String VisitorStatus);
	
	VisitorTagDetails findByVisitorId(long visitorId);
	
	VisitorTagDetails findByVisitorIdOrderByVisitorIdAsc(long visitorId);
	
	List<VisitorTagDetails> findByvehicleNumber(String vehicleNumber);
	
	List<VisitorTagDetails> findAll();
	
	List<VisitorTagDetails> findByflats(Flats flats);
	
	@Query(value="select * from visitor_tag_number v where v.visitor_status=?1 AND v.flats_flat_id=?2 and v.type= ?3 order by v.visitor_id asc",nativeQuery = true)
	List<VisitorTagDetails> getVisitorDetailsByflatNo(String visitorStatus,long flatId,String type);
	
	@Query(value="select * from visitor_tag_number where status=?1 order by visitor_id asc",nativeQuery = true)
	List<VisitorTagDetails> findAllActiveVisitors(boolean status);
	
	@Query(value="select * from visitor_tag_number where status=?1 and flats_flat_id=?2 order by visitor_id asc",nativeQuery = true)
	List<VisitorTagDetails> findAllOutVisitors(boolean status,long flatId);
	
	@Query(value="select * from visitor_tag_number where type=?1  order by visitor_id asc",nativeQuery = true)
	List<VisitorTagDetails> getVisitorDetailsBytype(String type);
	
	@Query(value="select * from visitor_tag_number where flats_flat_id=?1 and status=?2 order by visitor_id asc",nativeQuery = true)
	List<VisitorTagDetails> getVisitorDetailsByflatNo(long flatId,boolean status);
	
	@Query(value = "SELECT * FROM  pms.visitor_tag_number WHERE between CAST(expected_in_time AS DATE) = CAST(?1 AS DATE) and CAST(expected_out_time AS DATE) = CAST(?2 AS DATE) and status=?3 order by visitor_id asc", nativeQuery = true)
	 List<VisitorTagDetails> findByVisitorExpectedInDate(Date expectedOutTime,boolean status);
	
	@Query(value = "select * FROM pms.visitor_tag_number WHERE ?1 between expected_in_time and expected_out_time and status=?2 order by visitor_id asc", nativeQuery = true)
	 List<VisitorTagDetails> findByVisitorDate(Date expectedOutTime,boolean status);
	
	
	@Query(value="select * from visitor_tag_number where visitor_status=?1 and type=?2 order by visitor_id asc",nativeQuery = true)
	List<VisitorTagDetails> getAllVisitorsStatus(String visitorStatus,String type);
	
	@Query(value="select * from visitor_tag_number where expected_out_time=?1 order by visitor_id asc",nativeQuery = true)
	List<VisitorTagDetails> findAllVisitorsMoreThan24Hours(Date expectedOutTime);
	
	@Query(value="select * from visitor_tag_number where flats_flat_id=?1 and created_at=?2 order by visitor_id asc",nativeQuery = true)
	List<VisitorTagDetails> getByflatNo(long flatId,Date createdAt);

	@Query(value="select * from visitor_tag_number where slots_slots_id=?1  order by visitor_id asc",nativeQuery = true)
	List<VisitorTagDetails> getByslot(long slotId);
	
	@Query(value="select * from visitor_tag_number where  visitor_status=?1 and flats_flat_id=?2 order by visitor_id asc",nativeQuery = true)
	List<VisitorTagDetails> getInvoiceList(String visitorStatus,long flatId);
	
	@Query(value = "SELECT * FROM pms.visitor_tag_number WHERE MONTH(created_at) =?1  and YEAR(created_at) =?2 and flats_flat_id=?3 and visitor_status=?4 order by visitor_id asc", nativeQuery = true)
	List<VisitorTagDetails> getVisitorInvoice(long month,long year,long flatId,String visitorStatus);
}
