package com.revolut.transfer.mapper;

import com.revolut.transfer.dto.AccountRequest;
import com.revolut.transfer.dto.AccountResponse;
import com.revolut.transfer.formater.MoneyParser;
import com.revolut.transfer.model.Account;

public class AccountMapper {

	public static Account toAccount(AccountRequest accountRequest) {
		return new Account(accountRequest.name, accountRequest.currency, MoneyParser.parse("0.00"));
	}

	public static AccountResponse toAccountReponse(Account account) {
		AccountResponse response = new AccountResponse();
		response.id = account.getId();
		response.name = account.getName();
		response.currency = account.getCurrency();
		response.balance = MoneyParser.format(account.getBalance());
		return response;
	}	
}
