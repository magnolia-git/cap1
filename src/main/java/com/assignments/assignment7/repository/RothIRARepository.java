package com.assignments.assignment7.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignments.assignment7.models.IRA;
import com.assignments.assignment7.models.RothIRA;

//public interface RothIRARepository extends JpaRepository<RothIRA, Integer>{
//
//
//}
@Transactional
public interface RothIRARepository extends BankAccountRepository<RothIRA>{
	
}