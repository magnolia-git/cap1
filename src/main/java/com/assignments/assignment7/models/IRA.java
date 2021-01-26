package com.assignments.assignment7.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Entity(name = "IRA")
@Table(name = "IRA")
public class IRA extends BankAccount{
	@DecimalMin(value = "0.0", inclusive = false, message = "interest rate must be greater than zero")
	@DecimalMax(value = "1", inclusive = false, message = "interest rate must be less than one")
	double interestRate = 0.0500;

	public IRA() {
		super();
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	
}
