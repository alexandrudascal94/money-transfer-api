package com.revolut.transfer.service;

import java.math.BigDecimal;

import com.revolut.transfer.enumeration.Currency;
import com.revolut.transfer.model.Account;

public interface AccountService {
	
	public Account create(Account account);
	
	public Account findById(long accountId);

	public Account deposit(long accountId, BigDecimal amount, Currency currency);
	
	public Account withrow(long accountId, BigDecimal amount, Currency cuurrency);
	
	public void transfer(long fromAccountId, long toAccountId, BigDecimal amount, Currency currency);

}
