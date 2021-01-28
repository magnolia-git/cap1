package com.assignments.assignment7.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.assignments.assignment7.models.BankAccount;
import com.assignments.assignment7.models.CheckingAccount;

@NoRepositoryBean
public interface BankAccountRepository<T extends BankAccount> extends JpaRepository<T, Integer> {

}

/*
@NoRepositoryBean
public interface UserBaseRepository<T extends User> extends CrudRepository<T, Long> {

    T findByEmail(String email);
}

@Transactional
public interface PersonRepository extends UserBaseRepository<Person> {

}

@Transactional
public interface CompanyRepository extends UserBaseRepository<Company> {

}
*/