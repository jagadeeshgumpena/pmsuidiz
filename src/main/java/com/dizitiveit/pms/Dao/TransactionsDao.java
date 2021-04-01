package com.dizitiveit.pms.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dizitiveit.pms.model.Transactions;

public interface TransactionsDao extends JpaRepository<Transactions, Long> {
	
	Transactions findBypaymentId(String paymentId);
	List<Transactions> findByflatsFlatId(long flatId);

}
