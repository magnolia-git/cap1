package com.assignments.assignment7.models;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import Exceptions.ExceedsCombinedBalanceLimitException;
import Exceptions.NegativeBalanceException;

@Entity(name = "Transaction")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="bankAccount_id")
	DBAChecking dbaChecking;
	
	@ManyToOne
	@JoinColumn(name="checking_id")
	CheckingAccount checking;
	
	//BankAccount sourceAccount;
	double amount;
	Date transactionDate = new Date(); 
	String location;
	
	public Transaction() {
	}

	public abstract void process()
			throws NegativeBalanceException, ExceedsCombinedBalanceLimitException;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JsonBackReference(value="dbaChecking")
	public DBAChecking getDbaChecking() {
		return dbaChecking;
	}

	public void setDbaChecking(DBAChecking dbaChecking) {
		this.dbaChecking = dbaChecking;
	}

	@JsonBackReference(value="checking")
	public CheckingAccount getChecking() {
		return checking;
	}

	public void setChecking(CheckingAccount checking) {
		this.checking = checking;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
