package com.assignments.assignment7.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignments.assignment7.models.SavingsAccount;

public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Integer> {

}
