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

	@Override
	public void process()
			throws NegativeBalanceException, ExceedsCombinedBalanceLimitException {
		type = "Withdraw";
		if(dbaChecking != null) {
			location = "DBACheckingAccount";
			originAccountID = dbaChecking.getId();
			dbaChecking.withdraw(amount);
		}
		else if(checking != null) {
			location = "CheckingAccount";
			originAccountID = checking.getId();
			checking.withdraw(amount);
		}
		else if(savings != null) {
			location = "SavingsAccount";
			originAccountID = savings.getId();
			savings.withdraw(amount);
		}
		else if(cdAccount != null) {
			location = "CDAccount";
			originAccountID = cdAccount.getId();
			cdAccount.withdraw(amount);
		}
		else if(ira != null) {
			location = "IRA";
			originAccountID = ira.getId();
			ira.withdraw(amount);
		}
		else if(rothIRA != null) {
			location = "RothIRA";
			originAccountID = rothIRA.getId();
			rothIRA.withdraw(amount);
		}
		else if(rolloverIRA != null) {
			location = "RolloverIRA";
			originAccountID = rolloverIRA.getId();
			rolloverIRA.withdraw(amount);
		}
	}

	}