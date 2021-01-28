package com.assignments.assignment7.models;

import java.util.Date;

import Exceptions.ExceedsCombinedBalanceLimitException;
import Exceptions.ExceedsFraudSuspicionLimitException;
import Exceptions.NegativeBalanceException;

public class WithdrawTransaction extends Transaction{
	
	private String reason = "Withdraw";
	
	WithdrawTransaction(BankAccount targetAccount, double amount){
		super(targetAccount, amount);
		this.bankAccount = targetAccount;
		this.amount= amount;
		this.transactionDate = new Date();
		}

	@Override
	public void process()
			throws NegativeBalanceException, ExceedsCombinedBalanceLimitException, ExceedsFraudSuspicionLimitException {
		// TODO Auto-generated method stub
		
	}

	}
