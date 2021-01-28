package com.assignments.assignment7.models;

import java.util.Date;

import Exceptions.ExceedsCombinedBalanceLimitException;
import Exceptions.ExceedsFraudSuspicionLimitException;
import Exceptions.NegativeBalanceException;

public class DepositTransaction extends Transaction{

	DepositTransaction(BankAccount targetAccount, double amount/*, Date transactionDate*/){
		super(targetAccount, amount /*,transactionDate*/);
		this.bankAccount = targetAccount;
		this.amount = amount;
		this.transactionDate = new Date();
		
	}

	@Override
	public void process()
			throws NegativeBalanceException, ExceedsCombinedBalanceLimitException, ExceedsFraudSuspicionLimitException {
		// TODO Auto-generated method stub
		
	}

	
	
}
