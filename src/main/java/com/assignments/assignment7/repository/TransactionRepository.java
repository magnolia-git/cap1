package com.assignments.assignment7.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignments.assignment7.models.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer>{

}
