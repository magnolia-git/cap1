package com.assignments.assignment7.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignments.assignment7.models.Transaction;
import com.assignments.assignment7.models.TransferTransaction;

public interface TransferTransactionRepository extends JpaRepository<TransferTransaction, Integer>{

	List<Transaction> findByLocation(String location);
	
}
