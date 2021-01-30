package com.assignments.assignment7.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignments.assignment7.models.DepositTransaction;

public interface DepositTransactionRepository extends JpaRepository<DepositTransaction, Integer>{

}
