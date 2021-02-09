package com.assignments.assignment7.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.assignments.assignment7.models.DBAChecking;


public interface DBACheckingRepository extends JpaRepository<DBAChecking, Integer>{

	void deleteById(Integer id);
}