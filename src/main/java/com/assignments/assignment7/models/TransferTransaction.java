package com.assignments.assignment7.models;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import Exceptions.ExceedsCombinedBalanceLimitException;
import Exceptions.NegativeBalanceException;

@Entity(name = "TransferTransaction")
@Table(name = "TransferTransaction")
public class TransferTransaction extends Transaction{

//	BankAccount targetAccount;
//	BankAccount sourceAccount;
//	
	
	Integer targetAccountID;
	Integer sourceAccountID;

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
		else {
			//System.out.println("TRANSACTION AMOUNT:");
			type = "Transfer";
			sourceAccount.withdraw(amount);
			if(sourceAccount.typeOfAccount.contains("IRA"))
				amount = amount - (20 * amount) / 100;
			targetAccount.deposit(amount);
		}
	}

//	@JsonBackReference(value="targetAccount")
//	public BankAccount getTargetAccount() {
//		return targetAccount;
//	}
//
//	public void setTargetAccount(BankAccount targetAccount) {
//		this.targetAccount = targetAccount;
//	}
//
//	@JsonBackReference(value="sourceAccount")
//	public BankAccount getSourceAccount() {
//		return sourceAccount;
//	}
//
//	public void setSourceAccount(BankAccount sourceAccount) {
//		this.sourceAccount = sourceAccount;
//	}

	public Integer getTargetAccountID() {
		return targetAccountID;
	}

	public void setTargetAccountID(Integer targetAccountID) {
		this.targetAccountID = targetAccountID;
	}

	public Integer getSourceAccountID() {
		return sourceAccountID;
	}

	public void setSourceAccountID(Integer sourceAccountID) {
		this.sourceAccountID = sourceAccountID;
	}

	@JsonIgnore
	public List<BankAccount> getSourceAndTransferAccounts(){
		List<BankAccount> sourceAndTransfer = new ArrayList<BankAccount>();
		sourceAndTransfer.add(this.sourceAccount);
		sourceAndTransfer.add(this.targetAccount);
		return sourceAndTransfer;
	}

}