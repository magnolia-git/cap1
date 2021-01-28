package com.assignments.assignment7.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignments.assignment7.models.RolloverIRA;
import com.assignments.assignment7.models.RothIRA;

//public interface RolloverIRARepository extends JpaRepository<RolloverIRA, Integer>{
//
//
//}
@Transactional
public interface RolloverIRARepository extends BankAccountRepository<RolloverIRA>{
	
}