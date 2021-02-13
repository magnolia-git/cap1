package com.assignments.assignment7.models;

import java.util.Date;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.Table;

import Exceptions.ExceedsCombinedBalanceLimitException;
import Exceptions.NegativeBalanceException;

@Entity(name = "DepositTransaction")
@Table(name = "DepositTransaction")
public class DepositTransaction extends Transaction{

	public DepositTransaction() {
		super();
	}

	@Override
	public void process()
			throws NegativeBalanceException, ExceedsCombinedBalanceLimitException {
		type = "Deposit";
		if(dbaChecking != null) {
			location = "DBACheckingAccount";
			originAccountID = dbaChecking.getId();
			dbaChecking.deposit(amount);
		}
		else if(checking != null) {
			location = "CheckingAccount";
			originAccountID = checking.getId();
			checking.deposit(amount);
		}
		else if(savings != null) {
			location = "SavingsAccount";
			originAccountID = savings.getId();
			savings.deposit(amount);
		}
		else if(cdAccount != null) {
			location = "CDAccount";
			originAccountID = cdAccount.getId();
			cdAccount.deposit(amount);
		}
		else if(ira != null) {
			location = "IRA";
			originAccountID = ira.getId();
			ira.deposit(amount);
		}
		else if(rothIRA != null) {
			location = "RothIRA";
			originAccountID = rothIRA.getId();
			rothIRA.deposit(amount);
		}
		else if(rolloverIRA != null) {
			location = "RolloverIRA";
			originAccountID = rolloverIRA.getId();
			rolloverIRA.deposit(amount);
		}
	}
}