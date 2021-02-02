package com.assignments.assignment7.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Entity(name = "BankAccount")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BankAccount {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

	@ManyToOne
	@JoinColumn(name = "accountHolder_id") 
	private AccountHolder accountHolder;

	@Min(value = 0, message = "Balance must be atleast zero")
	double balance;
	
	Date openedOn = new Date(System.currentTimeMillis());;
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "dbaChecking", fetch = FetchType.LAZY)
	private List<Transaction> dbatransactions;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "checking", fetch = FetchType.LAZY)
	private List<Transaction> checkingtransactions;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "savings", fetch = FetchType.LAZY)
	private List<Transaction> savingstransactions;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cdAccount", fetch = FetchType.LAZY)
	private List<Transaction> cdAccoountTransactions;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ira", fetch = FetchType.LAZY)
	private List<Transaction> iraTransactions;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "rothIRA", fetch = FetchType.LAZY)
	private List<Transaction> rothIRATransactions;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "rolloverIRA", fetch = FetchType.LAZY)
	private List<Transaction> rolloverIRATransactions;
	
	public BankAccount() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@JsonBackReference(value="accountHolder")
	public AccountHolder getAccountHolder() {
		return accountHolder;
	}
	public void setAccountHolder(AccountHolder accountHolder) {
		this.accountHolder = accountHolder;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public Date getOpenedOn() {
		return openedOn;
	}
	public void setOpenedOn(Date openedOn) {
		this.openedOn = openedOn;
	}
	
	//@JsonManagedReference(value="dbaChecking")
	public List<Transaction> getDbatransactions() {
		return dbatransactions;
	}

	public void setDbatransactions(List<Transaction> dbatransactions) {
		this.dbatransactions = new ArrayList<Transaction>(dbatransactions);
	}

	public List<Transaction> getCheckingtransactions() {
		return checkingtransactions;
	}

	public void setCheckingtransactions(List<Transaction> checkingtransactions) {
		this.checkingtransactions = new ArrayList<Transaction>(checkingtransactions);
	}

	public List<Transaction> getSavingstransactions() {
		return savingstransactions;
	}

	public void setSavingstransactions(List<Transaction> savingstransactions) {
		this.savingstransactions = new ArrayList<Transaction>(savingstransactions);
	}

	public List<Transaction> getCdAccoountTransactions() {
		return cdAccoountTransactions;
	}

	public void setCdAccoountTransactions(List<Transaction> cdAccoountTransactions) {
		this.cdAccoountTransactions = new ArrayList<Transaction>(cdAccoountTransactions);
	}

	public List<Transaction> getIraTransactions() {
		return iraTransactions;
	}

	public void setIraTransactions(List<Transaction> iraTransactions) {
		this.iraTransactions = new ArrayList<Transaction>(iraTransactions);
	}

	public List<Transaction> getRothIRATransactions() {
		return rothIRATransactions;
	}

	public void setRothIRATransactions(List<Transaction> rothIRATransactions) {
		this.rothIRATransactions = new ArrayList<Transaction>(rothIRATransactions);
	}

	public List<Transaction> getRolloverIRATransactions() {
		return rolloverIRATransactions;
	}

	public void setRolloverIRATransactions(List<Transaction> rolloverIRATransactions) {
		this.rolloverIRATransactions = new ArrayList<Transaction>(rolloverIRATransactions);
	}

	public void withdraw(double amount) {
		this.balance -= amount;
	}

	public void deposit(double amount) {
		this.balance += amount;
	}
}
