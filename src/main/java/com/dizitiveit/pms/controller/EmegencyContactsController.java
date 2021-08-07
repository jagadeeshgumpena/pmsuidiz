package com.dizitiveit.pms.controller;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.Dao.EmergencyContactsDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.Dao.SecurityShiftsDao;
import com.dizitiveit.pms.model.EmergencyContacts;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.SecurityShifts;
import com.dizitiveit.pms.pojo.BuildingSecurityPojo;
import com.dizitiveit.pms.pojo.EmergencyContactsPojo;
import com.dizitiveit.pms.pojo.SecurityShiftsPojo;



@RestController
@RequestMapping("/emergencyContacts")
public class EmegencyContactsController {
	
	@Autowired
	private EmergencyContactsDao emergencyContactsDao;

	@Autowired
	private ResponsesDao responsesDao;
	
	@Autowired
	private SecurityShiftsDao securityShiftsDao;
	
	@PostMapping("/saveEmergencyContacts")
	public ResponseEntity<?> saveEmergencyContacts(@RequestBody EmergencyContacts emergencyContacts){
		EmergencyContacts emegencyContactsExisting = emergencyContactsDao.findByMobile(emergencyContacts.getMobile());
		if(emegencyContactsExisting==null)
		{
			emergencyContacts.setCreatedAt(new Date());
			emergencyContacts.setEmergencyStatus(true);
			emergencyContactsDao.save(emergencyContacts);
			Responses responses = responsesDao.findById(84);
			  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
		}
		else {
			Responses responses = responsesDao.findById(14);
			  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
		}
	}
	
	@GetMapping("/getEmergencyContacts")
	public ResponseEntity<?> getEmergencyContacts()
	{
		List<EmergencyContacts> emergencyContactsList = emergencyContactsDao.findByEmergencyStatus(true);
		
		List<EmergencyContactsPojo> emergencyContactsPojoList = new ArrayList();
		for(EmergencyContacts emergencyContacts : emergencyContactsList)
		{
			EmergencyContactsPojo emergencyContactsPojo = new EmergencyContactsPojo();
			emergencyContactsPojo.setId(emergencyContacts.getEmergencyId());
			emergencyContactsPojo.setEmergencyType(emergencyContacts.getEmergencyType());
			emergencyContactsPojo.setFirstName(emergencyContacts.getFirstName());
			emergencyContactsPojo.setLastName(emergencyContacts.getLastName());
			emergencyContactsPojo.setMobile(emergencyContacts.getMobile());
			emergencyContactsPojoList.add(emergencyContactsPojo);
		}
		Date date= new Date();
		Instant inst = date.toInstant();
		 LocalDate localDate = inst.atZone(ZoneId.systemDefault()).toLocalDate();
		  Instant dayInst = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
		   Date day = Date.from(dayInst);
		  System.out.println(day);
		   Date dateTomorrow=DateUtils.addDays(day, 1);
		List<SecurityShifts> securityShitsList = securityShiftsDao.findBySecurityToday(day, true);
		List<SecurityShiftsPojo> listsecurityShiftPojo= new ArrayList();
		for(SecurityShifts securityShift : securityShitsList )
		{
			EmergencyContactsPojo emergencyContactsPojo = new EmergencyContactsPojo();
			emergencyContactsPojo.setId(securityShift.getBuildingSecurity().getSecurityId());
			emergencyContactsPojo.setEmergencyType("Security");
			emergencyContactsPojo.setFirstName(securityShift.getBuildingSecurity().getFirstName());
			emergencyContactsPojo.setLastName(securityShift.getBuildingSecurity().getLastName());
			emergencyContactsPojo.setMobile(securityShift.getBuildingSecurity().getMobile());
			emergencyContactsPojoList.add(emergencyContactsPojo);
		}
		
		HashMap<String,List<EmergencyContactsPojo>> response = new HashMap<String,List<EmergencyContactsPojo>>();
		response.put("emergencyContactsPojo", emergencyContactsPojoList);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/deactivateEmergency/{emergencyId}")
	public ResponseEntity<?> deactivateEmergency(@PathVariable long emergencyId)
	{
	     EmergencyContacts emergencyContacts = 	emergencyContactsDao.findById(emergencyId);
	     emergencyContacts.setEmergencyStatus(false);
			emergencyContactsDao.save(emergencyContacts);
			Responses responses = responsesDao.findById(86);
			  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));	
	}
	
	@GetMapping("/getDeactiveList")
	public ResponseEntity<?> getDeactiveList()
	{
		List<EmergencyContacts> emergencyContactsList = emergencyContactsDao.findByEmergencyStatus(false);
		List<EmergencyContactsPojo> emergencyContactsPojoList = new ArrayList();
		for(EmergencyContacts emergencyContact : emergencyContactsList)
		{
			EmergencyContactsPojo emergencyContactsPojo = new EmergencyContactsPojo();
			emergencyContactsPojo.setId(emergencyContact.getEmergencyId());
			emergencyContactsPojo.setEmergencyType(emergencyContact.getEmergencyType());
			emergencyContactsPojo.setFirstName(emergencyContact.getFirstName());
			emergencyContactsPojo.setLastName(emergencyContact.getLastName());
			emergencyContactsPojo.setMobile(emergencyContact.getMobile());
			emergencyContactsPojoList.add(emergencyContactsPojo);
		}

		HashMap<String,List<EmergencyContactsPojo>> response = new HashMap<String,List<EmergencyContactsPojo>>();
		response.put("emergencyContactsPojo", emergencyContactsPojoList);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/activateEmergency/{emergencyId}")
	public ResponseEntity<?> activateEmergency(@PathVariable long emergencyId)
	{
		EmergencyContacts emergencyContacts = 	emergencyContactsDao.findById(emergencyId);
	     emergencyContacts.setEmergencyStatus(true);
			emergencyContactsDao.save(emergencyContacts);
			Responses responses = responsesDao.findById(87);
			  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));	
	}
}
