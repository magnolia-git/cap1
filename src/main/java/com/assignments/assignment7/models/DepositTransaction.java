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
		// TODO Auto-generated method stub
		if(dbaChecking != null) {
			location = "DBACheckingAccount";
			dbaChecking.deposit(amount);
		}
		else if(checking != null) {
			location = "CheckingAccount";
			checking.deposit(amount);
		}
		else if(savings != null) {
			location = "SavingsAccount";
			savings.deposit(amount);
		}
		else if(cdAccount != null) {
			location = "CDAccount";
			cdAccount.deposit(amount);
		}
		else if(ira != null) {
			location = "IRA";
			ira.deposit(amount);
		}
		else if(rothIRA != null) {
			location = "RothIRA";
			rothIRA.deposit(amount);
		}
		else if(rolloverIRA != null) {
			location = "RolloverIRA";
			rolloverIRA.deposit(amount);
		}
	}
}