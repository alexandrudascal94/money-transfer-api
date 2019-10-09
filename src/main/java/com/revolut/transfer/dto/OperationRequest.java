package com.revolut.transfer.dto;

import com.revolut.transfer.enumeration.Currency;

public class OperationRequest {
	public Currency currency;
	public String amount;
	
	public OperationRequest(Currency currency, String amount) {
		this.currency = currency;
		this.amount = amount;
	}	
}
