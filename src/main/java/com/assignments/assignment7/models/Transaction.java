package com.assignments.assignment7.models;

import java.util.Date;

import javax.persistence.*;

import Exceptions.ExceedsCombinedBalanceLimitException;
import Exceptions.NegativeBalanceException;

@Entity(name = "Transaction")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="dBAChecking_id")
	DBAChecking bankAccount;
	
	//BankAccount sourceAccount;
	double amount;
	Date transactionDate; 

	
//	Transaction(BankAccount sourceAccount, BankAccount bankAccount, double amount) {
//		this.sourceAccount = sourceAccount;
//		this.bankAccount = bankAccount;
//		this.amount = amount;
//		this.transactionDate = new Date();
//	}

	public Transaction() {
	}

//	Transaction(BankAccount bankAccount, double amount) {
//		this.bankAccount = bankAccount;
//		this.amount = amount;
//		this.transactionDate = new Date();
//	}

	public abstract void process()
			throws NegativeBalanceException, ExceedsCombinedBalanceLimitException;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(DBAChecking bankAccount) {
		this.bankAccount = bankAccount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

}
