package com.assignments.assignment7.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignments.assignment7.models.CDAccount;
import com.assignments.assignment7.models.IRA;

//public interface IRARepository extends JpaRepository<IRA, Integer>{
//
//}
@Transactional
public interface IRARepository extends BankAccountRepository<IRA>{
	
}