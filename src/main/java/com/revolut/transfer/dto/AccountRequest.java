package com.revolut.transfer.dto;

import com.revolut.transfer.enumeration.Currency;

public class AccountRequest {		
	public String name;
	public Currency currency;	
	
	public AccountRequest(String name, Currency currency) {
		this.name = name;
		this.currency = currency;
	}
}
