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
	
	@ManyToOne
	@JoinColumn(name="savings_id")
	SavingsAccount savings;
	
	@ManyToOne
	@JoinColumn(name="cdAccount_id")
	CDAccount cdAccount;
	
	@ManyToOne
	@JoinColumn(name="ira_id")
	IRA ira;
	
	@ManyToOne
	@JoinColumn(name="rothIRA_id")
	RothIRA rothIRA;
	
	@ManyToOne
	@JoinColumn(name="rolloverIRA_id")
	RolloverIRA rolloverIRA;
	
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

	@JsonBackReference(value="savings")
	public SavingsAccount getSavings() {
		return savings;
	}

	public void setSavings(SavingsAccount savings) {
		this.savings = savings;
	}

	@JsonBackReference(value="cdAccountTransaction")
	public CDAccount getCdAccount() {
		return cdAccount;
	}

	public void setCdAccount(CDAccount cdAccount) {
		this.cdAccount = cdAccount;
	}

	@JsonBackReference(value="ira")
	public IRA getIra() {
		return ira;
	}

	public void setIra(IRA ira) {
		this.ira = ira;
	}

	@JsonBackReference(value="rothIRA")
	public RothIRA getRothIRA() {
		return rothIRA;
	}

	public void setRothIRA(RothIRA rothIRA) {
		this.rothIRA = rothIRA;
	}

	@JsonBackReference(value="rolloverIRA")
	public RolloverIRA getRolloverIRA() {
		return rolloverIRA;
	}

	public void setRolloverIRA(RolloverIRA rolloverIRA) {
		this.rolloverIRA = rolloverIRA;
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
