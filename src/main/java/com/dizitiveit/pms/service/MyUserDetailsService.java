package com.dizitiveit.pms.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dizitiveit.pms.Dao.FlatOwnersDao;
import com.dizitiveit.pms.Dao.FlatResidenciesDao;
import com.dizitiveit.pms.Dao.UsersDao;
import com.dizitiveit.pms.model.FlatOwners;
import com.dizitiveit.pms.model.MyUserDetails;
import com.dizitiveit.pms.model.Users;

@Service
public class MyUserDetailsService implements UserDetailsService{
	
	 @Autowired 
	  private UsersDao usersDao;
	 
	 @Autowired 
	  private PasswordEncoder passwordEncoder;
	 
	 @Autowired
	 private FlatOwnersDao flatOwnersDao;
	 
	 @Autowired
	 private FlatResidenciesDao flatResidenciesDao;
	
	 @Override 
	  public UserDetails loadUserByUsername(String userName) throws
	  UsernameNotFoundException {
		 System.out.println("Username is"+userName);
	  
	  Optional<Users> user = usersDao.findByEmailOrMobile(userName, userName);
	  
	  System.out.println(user.toString());
	  
	  user.orElseThrow(() -> new UsernameNotFoundException("Not found: " +
	  userName));
	  
	 return user.map(MyUserDetails::new).get();
	  
	 
	  
	  }
	  
	  @Transactional
	  public Long registerNewUser(Users users)
	  {
		  Users newusers = new Users();

	  newusers.setEmail(users.getEmail());
	  newusers.setMobile(users.getMobile());  
	   newusers.setRoles(users.getRoles()); 
	 newusers.setActive(users.isActive());
	 newusers.setFirstName(users.getFirstName());
	  newusers.setLastName(users.getLastName());
	  newusers.setCreatedAt(new Date());
	 newusers.setMobileVerification(users.getMobileVerification());
	  newusers.setUserId(users.getUserId());
	  newusers.setPassword(passwordEncoder.encode(users.getPassword()));
	 // System.out.println(newusers.getPassword());
	  Users user = usersDao.save(newusers);
	  
	  Date date = new Date(); 
	  SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS"); 
	  String strDate = formatter.format(date); 
	  System.out.println("Registration No: " + strDate);
	  
	  return user.getUserId();
	  
	  }
	  
	  public Users getAuthUser() { 
		  Authentication authentication =
	  SecurityContextHolder.getContext().getAuthentication();
		  String currentPrincipalName = authentication.getName();
	  System.out.println(currentPrincipalName);
	  
	  try { 
		  Users user = usersDao.findByEmail(currentPrincipalName);
		  
	  
	  if (Class.forName("com.dizitiveit.pms.model.Users").isInstance(user)) 
	  {
	  return user; 
	  } 
	  else { 
		  user = usersDao.findByMobile(currentPrincipalName);
	  if(Class.forName("com.dizitiveit.pms.model.Users").isInstance(user)) 
	  { 
	  return user;
	  } 
	  } 
	  } catch (ClassNotFoundException E) {
		  E.printStackTrace();
		  }
	  catch (Exception E) {
		  E.printStackTrace();
		  } 
	  return null; 
		  }

	}

