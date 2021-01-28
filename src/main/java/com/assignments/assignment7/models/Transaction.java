package com.assignments.assignment7.models;

import java.util.Date;

import javax.persistence.*;

import Exceptions.ExceedsCombinedBalanceLimitException;
import Exceptions.ExceedsFraudSuspicionLimitException;
import Exceptions.NegativeBalanceException;

@Entity(name = "Transaction")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="bankAccount_id")
	BankAccount bankAccount;
	
	//BankAccount sourceAccount;
	double amount;
	Date transactionDate; 

//	Transaction(BankAccount sourceAccount, BankAccount bankAccount, double amount) {
//		this.sourceAccount = sourceAccount;
//		this.bankAccount = bankAccount;
//		this.amount = amount;
//		this.transactionDate = new Date();
//	}

	Transaction(BankAccount bankAccount, double amount) {
		this.bankAccount = bankAccount;
		this.amount = amount;
		this.transactionDate = new Date();
	}



	public abstract void process()
			throws NegativeBalanceException, ExceedsCombinedBalanceLimitException, ExceedsFraudSuspicionLimitException;

}

