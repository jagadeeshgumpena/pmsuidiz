package com.dizitiveit.pms.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.Dao.BuildingSecurityDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.Dao.SecurityLoginDetailsDao;
import com.dizitiveit.pms.model.BuildingSecurity;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.SecurityLoginDetails;
@RestController
@RequestMapping("/securityLogin")
public class SecurityLoginDetailsController {

	 @Autowired
	   private BuildingSecurityDao buildingSecurityDao;
	 
	 @Autowired
	   private SecurityLoginDetailsDao securityLoginDetailsDao;
	 
	@Autowired
	private ResponsesDao responsesDao;
	 
	 @PostMapping(value="/SecurityLogInTime/{mobile}/{purpose}")
	    public ResponseEntity<?> SecurityLogInTime(@PathVariable String mobile,@PathVariable String purpose){
	    	 BuildingSecurity buildingSecurity = buildingSecurityDao.findByMobile(mobile);
	    	 SecurityLoginDetails securityLoginExist = securityLoginDetailsDao.findByShiftSlotBySecurityId(buildingSecurity.getSecurityId(),"IN");
	    	 	if(securityLoginExist==null) {
				SecurityLoginDetails securityLogin = new SecurityLoginDetails();
				securityLogin.setInTime(new Date());
				securityLogin.setPurpose(purpose);
				securityLogin.setBuildingSecurity(buildingSecurity);
				securityLoginDetailsDao.save(securityLogin);		
				 Responses responses = responsesDao.findById(41);
					return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
	 }
	 else {
		 Responses responses = responsesDao.findById(42);
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
	    }
	 }
	
	 @PostMapping(value="/SecurityLogoutTime/{mobile}/{purpose}")
	    public ResponseEntity<?> logoutTime(@PathVariable String mobile,@PathVariable String purpose){
	    	 BuildingSecurity buildingSecurity = buildingSecurityDao.findByMobile(mobile);
				if(mobile!=null) {
				SecurityLoginDetails securityLogin = securityLoginDetailsDao.findByShiftSlotBySecurityId(buildingSecurity.getSecurityId(),"IN");
				if(securityLogin!=null) {
				securityLogin.setOutTime(new Date());
				securityLogin.setPurpose(purpose);
				//securityLogin.setBuildingSecurity(buildingSecurity);
				securityLoginDetailsDao.save(securityLogin);	
				 Responses responses = responsesDao.findById(43);
					return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
				}
				else {
					 Responses responses = responsesDao.findById(46);
						return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
				}
				}
				else {
				 Responses responses = responsesDao.findById(3);
					return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
	    }
      }
	 }
