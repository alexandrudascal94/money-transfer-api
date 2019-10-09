package com.revolut.transfer.dto;

import com.revolut.transfer.enumeration.Currency;

public class TransferResponse {
	public String message;
	public long fromAccount;
	public long toAccount;
	public String amount;
	public Currency currency;
	
	public TransferResponse(String message, long fromAccount, long toAccount, String amount, Currency currency) {
		this.message = message;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.amount = amount;
		this.currency = currency;
	}

}
