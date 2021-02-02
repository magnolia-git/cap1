package com.assignments.assignment7.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import Exceptions.ExceedsCombinedBalanceLimitException;
import Exceptions.NegativeBalanceException;

@Entity(name = "WithdrawTransaction")
@Table(name = "WithdrawTransaction")
public class WithdrawTransaction extends Transaction{

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
		if(dbaChecking != null) {
			location = "DBACheckingAccount";
			dbaChecking.withdraw(amount);
		}
		else if(checking != null) {
			location = "CheckingAccount";
			checking.withdraw(amount);
		}
		else if(savings != null) {
			location = "SavingsAccount";
			savings.withdraw(amount);
		}
		else if(cdAccount != null) {
			location = "CDAccount";
			cdAccount.withdraw(amount);
		}
		else if(ira != null) {
			location = "IRA";
			ira.withdraw(amount);
		}
		else if(rothIRA != null) {
			location = "RothIRA";
			rothIRA.withdraw(amount);
		}
		else if(rolloverIRA != null) {
			location = "RolloverIRA";
			rolloverIRA.withdraw(amount);
		}
	}

	}