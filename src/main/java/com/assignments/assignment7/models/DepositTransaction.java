package com.assignments.assignment7.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import Exceptions.ExceedsCombinedBalanceLimitException;
import Exceptions.NegativeBalanceException;

@Entity(name = "DepositTransaction")
@Table(name = "DepositTransaction")
public class DepositTransaction extends Transaction{

	//private BankAccount targetAccount;
	
	public DepositTransaction() {
		super();
	}
//	DepositTransaction(BankAccount targetAccount, double amount/*, Date transactionDate*/){
//		super(targetAccount, amount /*,transactionDate*/);
//		this.bankAccount = targetAccount;
//		this.amount = amount;
//		this.transactionDate = new Date();
//	}

	@Override
	public void process()
			throws NegativeBalanceException, ExceedsCombinedBalanceLimitException {
		// TODO Auto-generated method stub
		bankAccount.deposit(amount);
	}	
}