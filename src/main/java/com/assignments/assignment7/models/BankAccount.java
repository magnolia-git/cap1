package com.assignments.assignment7.models;

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
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Entity(name = "BankAccount")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class BankAccount {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

	@ManyToOne
	@JoinColumn(name = "accountHolder_id") 
	private AccountHolder accountHolder;
	 
	@Min(value = 0, message = "Balance must be atleast zero")
	double balance;
	
	Date openedOn;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "bankAccount", fetch = FetchType.LAZY)
	private List<Transaction> transaction;
	
	public BankAccount() {
		this.openedOn = new Date(System.currentTimeMillis());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@JsonBackReference
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

	public void withdraw(double amount) {
		// TODO Auto-generated method stub
		
	}

	public void deposit(double amount) {
		// TODO Auto-generated method stub
		
	}
}
