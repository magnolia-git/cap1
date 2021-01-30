package com.assignments.assignment7.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import Exceptions.ExceedsCombinedBalanceLimitException;
import Exceptions.NegativeBalanceException;

@Entity(name = "WithdrawTransaction")
@Table(name = "WithdrawTransaction")
public class WithdrawTransaction extends Transaction{
	
	private String reason = "Withdraw";
	
	public WithdrawTransaction() {
		super();
	}

//	WithdrawTransaction(BankAccount targetAccount, double amount){
//		super(targetAccount, amount);
//		this.bankAccount = targetAccount;
//		this.amount= amount;
//		this.transactionDate = new Date();
//		}

	@Override
	public void process()
			throws NegativeBalanceException, ExceedsCombinedBalanceLimitException {
		// TODO Auto-generated method stub
		
	}

	}