package com.assignments.assignment7.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignments.assignment7.models.Transaction;
import com.assignments.assignment7.models.WithdrawTransaction;

public interface WithdrawTransactionRepository extends JpaRepository<WithdrawTransaction, Integer>{

	List<Transaction> findByLocation(String location);
}
