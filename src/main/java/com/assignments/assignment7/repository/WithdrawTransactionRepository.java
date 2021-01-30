package com.assignments.assignment7.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignments.assignment7.models.WithdrawTransaction;

public interface WithdrawTransactionRepository extends JpaRepository<WithdrawTransaction, Integer>{

}
