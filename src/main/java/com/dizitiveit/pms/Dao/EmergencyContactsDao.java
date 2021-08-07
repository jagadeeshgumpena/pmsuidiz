package com.dizitiveit.pms.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dizitiveit.pms.model.EmergencyContacts;

public interface EmergencyContactsDao extends JpaRepository<EmergencyContacts,Long>{

	EmergencyContacts findByMobile(String mobile);
	EmergencyContacts findById(long emergencyId);
	List<EmergencyContacts> findByEmergencyStatus(boolean emergencyStatus);
}
