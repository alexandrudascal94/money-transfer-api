package com.revolut.transfer.dto;

import com.revolut.transfer.enumeration.Currency;

public class AccountResponse {
	public Long id;
	public String name;
	public Currency currency;
	public String balance;
}
