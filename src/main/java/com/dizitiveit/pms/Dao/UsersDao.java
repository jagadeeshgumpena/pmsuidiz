package com.dizitiveit.pms.Dao;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.Users;

public interface UsersDao extends JpaRepository<Users, Long>  {
	
	Users findById(long userId);
	  Optional<Users> findByEmailOrMobile(String Email, String mobile);
	  
	  Users findByEmail(String Email);
	  Users findByUserId(String userId);
	 // List<Users> findByType(String type);
	  Users findByMobile(@Param ("mobile") String mobile);
	//Users findByFlats(Flats flat);   
	
	  List<Users> findByRoles(String roles);
	  
	  @Query(value = "select *  FROM users where roles in(?1,?2) ", nativeQuery = true)
	    List<Users> findByTwoRoles(String ROLE_MANAGER,String ROLE_SECURITY_MANAGER);
	  
	  @Query(value = "select *  FROM pms.users where roles=?1 and active=?2 ", nativeQuery = true)
	    List<Users> findByRoles(String ROLE_SECURITY,boolean active);
	
	

}
