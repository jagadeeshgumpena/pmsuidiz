package com.dizitiveit.pms.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.Dao.BuildingSecurityDao;
import com.dizitiveit.pms.Dao.ReLoginDetailsDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.Dao.SecurityLoginDetailsDao;
import com.dizitiveit.pms.Dao.SecurityShiftsDao;
import com.dizitiveit.pms.model.BuildingSecurity;
import com.dizitiveit.pms.model.ReLoginDetails;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.SecurityShifts;
import com.dizitiveit.pms.model.Users;
import com.dizitiveit.pms.model.VehicleDetails;
import com.dizitiveit.pms.pojo.BuildingSecurityPojo;
import com.dizitiveit.pms.pojo.SecurityShiftsPojo;
import com.dizitiveit.pms.service.MyUserDetailsService;

@RestController
@RequestMapping("/security")
public class BuildingSecurityController {
	
	@Autowired
	private BuildingSecurityDao buildingSecurityDao;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private ReLoginDetailsDao reLoginDetailsDao;
	
	@Autowired
	private ResponsesDao responsesDao;
	
	@Autowired
	private SecurityShiftsDao securityShiftsDao; 
	
	@Autowired
	private SecurityLoginDetailsDao securityLoginDetailsDao; 

	@GetMapping("/getAllSecurityDetails")
	public ResponseEntity<?> getAllSecurityDetails(){
		List<BuildingSecurity> buildingSecurity = buildingSecurityDao.findAll();
		 HashMap<String, List<BuildingSecurity>> response = new HashMap<String,List<BuildingSecurity>>();
         response.put("buildingSecurity",buildingSecurity);
	 return ResponseEntity.ok(response);
	}
	
	@GetMapping("/getSecurityDetails")
	public ResponseEntity<?> getSecurityDetails(){
		Users users = userDetailsService.getAuthUser();
		BuildingSecurity buildingSecurity = buildingSecurityDao.findByMobile(users.getMobile());
		ReLoginDetails reLoginDetails = new ReLoginDetails();
		reLoginDetails.setReLoginTime(new Date());
		reLoginDetails.setBuildingSecurity(buildingSecurity);
		reLoginDetailsDao.save(reLoginDetails);
		return ResponseEntity.status(200).body(buildingSecurity);
		
	}
	
	@DeleteMapping("/deleteSecurityGaurd/{securityId}")
	public ResponseEntity<?> deleteSecurityGaurd(@PathVariable long securityId){
		this.securityLoginDetailsDao.removeSecurityLoginDetailsBysecurityId(securityId);
		this.securityShiftsDao.removeSecurityShitsBysecurityId(securityId);
		this.reLoginDetailsDao.removeReLoginDetailsBysecurityId(securityId);
		BuildingSecurity buildingSecurity = buildingSecurityDao.deleteById(securityId);
		 Responses responses = responsesDao.findById(54);
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		   }
		
	@GetMapping("/getSecurityShiftDetails/{securityId}")
	public ResponseEntity<?> getSecurityShiftDetails(@PathVariable long securityId){
		Users users = userDetailsService.getAuthUser();
		BuildingSecurityPojo buildingSecurityPojo = new BuildingSecurityPojo();
		BuildingSecurity buildingSecurity = buildingSecurityDao.findBySecurityId(securityId);
		buildingSecurityPojo.setBuildingSecurity(buildingSecurity);
		List<SecurityShifts> securityShifts=securityShiftsDao.findByBuildingSecurity(buildingSecurity);
		 Responses responses = responsesDao.findById(54);
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));

	}
	
	@GetMapping("/securityShifts/{securityId}")
	public ResponseEntity<?> securityShifts(@PathVariable long securityId){
		List<SecurityShifts> securityShiftsList =  securityShiftsDao.findByShiftSlotBySecurityId(securityId, true);
		List<SecurityShiftsPojo> listsecurityShiftPojo= new ArrayList();
		for(SecurityShifts securityShift : securityShiftsList)
		{
		SecurityShiftsPojo shiftsPojo = new SecurityShiftsPojo();
		shiftsPojo.setSecurityId(securityShift.getBuildingSecurity().getSecurityId());
		shiftsPojo.setShiftSlot(securityShift.getShiftSlot());
		shiftsPojo.setStartTime(securityShift.getStartTime());
		shiftsPojo.setEndTime(securityShift.getEndTime());
		shiftsPojo.setStartDate(securityShift.getStartDate());
		shiftsPojo.setEndDate(securityShift.getEndDate());
		listsecurityShiftPojo.add(shiftsPojo);
		}
		 HashMap<String,List<SecurityShiftsPojo>> response = new HashMap<String,List<SecurityShiftsPojo>>();
			response.put("listsecurityShiftPojo", listsecurityShiftPojo);
			return ResponseEntity.ok(response);
	}
	
	@GetMapping("/securityShiftsHistory/{securityId}")
	public ResponseEntity<?> securityShiftsHistory(@PathVariable long securityId){
		List<SecurityShifts> securityShiftsList =  securityShiftsDao.findByShiftSlotBySecurityId(securityId, false);
		List<SecurityShiftsPojo> listsecurityShiftPojo= new ArrayList();
		for(SecurityShifts securityShift : securityShiftsList)
		{
		SecurityShiftsPojo shiftsPojo = new SecurityShiftsPojo();
		shiftsPojo.setSecurityId(securityShift.getBuildingSecurity().getSecurityId());
		shiftsPojo.setShiftSlot(securityShift.getShiftSlot());
		shiftsPojo.setStartTime(securityShift.getStartTime());
		shiftsPojo.setEndTime(securityShift.getEndTime());
		shiftsPojo.setStartDate(securityShift.getStartDate());
		shiftsPojo.setEndDate(securityShift.getEndDate());
		listsecurityShiftPojo.add(shiftsPojo);
		}
		 HashMap<String,List<SecurityShiftsPojo>> response = new HashMap<String,List<SecurityShiftsPojo>>();
			response.put("listsecurityShiftPojo", listsecurityShiftPojo);
			return ResponseEntity.ok(response);
	}
}
