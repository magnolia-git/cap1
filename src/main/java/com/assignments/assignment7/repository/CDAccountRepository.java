package com.assignments.assignment7.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignments.assignment7.models.CDAccount;
import com.assignments.assignment7.models.CheckingAccount;

//public interface CDAccountRepository extends JpaRepository<CDAccount, Integer>{
//
//}

@Transactional
public interface CDAccountRepository extends BankAccountRepository<CDAccount>{

}