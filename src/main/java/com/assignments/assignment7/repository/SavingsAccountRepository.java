package com.assignments.assignment7.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignments.assignment7.models.CheckingAccount;
import com.assignments.assignment7.models.SavingsAccount;
//
//public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Integer> {
//
//}
@Transactional
public interface SavingsAccountRepository extends BankAccountRepository<SavingsAccount>{
	
}