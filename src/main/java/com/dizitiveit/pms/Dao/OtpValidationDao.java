package com.dizitiveit.pms.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dizitiveit.pms.model.OtpValidation;



public interface OtpValidationDao extends JpaRepository<OtpValidation, Long>{

	OtpValidation findById(long otpId);

	OtpValidation findBymobile(String mobile);

	 //OtpValidation findBymobileAndstatus(String mobile,String status);
}
