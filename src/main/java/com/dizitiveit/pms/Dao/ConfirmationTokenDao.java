
  package com.dizitiveit.pms.Dao;
  
  import org.springframework.data.jpa.repository.JpaRepository;

import com.dizitiveit.pms.model.ConfirmationToken;
import com.dizitiveit.pms.model.Users;
  
  
  
  public interface ConfirmationTokenDao extends JpaRepository<ConfirmationToken,Long>{
  
  ConfirmationToken findByConfirmationToken(String confirmationToken);

ConfirmationToken findByusers(Users users);

ConfirmationToken findByConfirmationTokenAndUsers(String confirmationToken, Users users);
  
  }
 
