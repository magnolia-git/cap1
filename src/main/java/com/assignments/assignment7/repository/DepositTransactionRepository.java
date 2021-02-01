package com.assignments.assignment7.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignments.assignment7.models.AccountHolder;
import com.assignments.assignment7.models.CDOffering;
import com.assignments.assignment7.models.DepositTransaction;
import com.assignments.assignment7.models.Transaction;

public interface DepositTransactionRepository extends JpaRepository<DepositTransaction, Integer>{

	List<Transaction> findByLocation(String location);
	
}
