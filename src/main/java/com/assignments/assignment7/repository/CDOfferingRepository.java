package com.assignments.assignment7.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignments.assignment7.models.CDOffering;

public interface CDOfferingRepository extends JpaRepository<CDOffering, Integer> {

	CDOffering findByTerm(Integer term);
}
