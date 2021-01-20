package com.assignments.assignment7.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignments.assignment7.models.AccountHolder;

public interface AccountHolderRepository extends JpaRepository<AccountHolder, Integer>{

	Optional<AccountHolder> findById(Integer Id);
}
