package com.assignments.assignment7.models;


import java.util.Date;

import Exceptions.ExceedsCombinedBalanceLimitException;
import Exceptions.NegativeBalanceException;

public class TransferTransaction extends Transaction{

//	TransferTransaction(BankAccount bankAccount, double amount) {
//		super(bankAccount, amount);
//		// TODO Auto-generated constructor stub
//	}

	private double amount;
	private BankAccount targetAccount;
	private BankAccount sourceAccount;

//	 public TransferTransaction(BankAccount sourceAccount, BankAccount targetAccount, double amount) {
//		super(sourceAccount, amount);
//		
//	}

	public TransferTransaction() {
		super();

	}

	@Override
	public void process()
			throws NegativeBalanceException, ExceedsCombinedBalanceLimitException {
		if (amount < 0) {
			throw new NegativeBalanceException("WARNING! Can not transfer a negative amount");
		}
		else if (sourceAccount.getBalance() < amount) {
			throw new ExceedsCombinedBalanceLimitException("WARNING! Your amount has exceeded the acceptable limit");
		}
//		else if (amount > 1000) {
//			throw new ExceedsFraudSuspicionLimitException("WARNING! We are sorry this transaction can not completed");
//		}
		else {
			System.out.println("TRANSACTION AMOUNT:");
			sourceAccount.withdraw(amount);
			targetAccount.deposit(amount);
		}
	}
}