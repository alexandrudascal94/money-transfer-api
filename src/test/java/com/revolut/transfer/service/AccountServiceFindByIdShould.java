package com.revolut.transfer.service;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.Test;

import com.revolut.transfer.exception.AccountNotFoundException;
import com.revolut.transfer.model.Account;
import com.revolut.transfer.repository.AccountRepository;

public class AccountServiceFindByIdShould {

	AccountRepository accountRepository = mock(AccountRepository.class);;

	ExchangeService exchangeService = mock(ExchangeService.class);

	AccountService accountService = new AccountServiceImpl(accountRepository, exchangeService);

	@Test(expected = AccountNotFoundException.class)
	public void throw_AccountNotFoundException_whenRepositoryDoesNotFindIt() {
		doReturn(Optional.ofNullable(null)).when(accountRepository)
			.findById(anyLong());

		accountService.findById(anyLong());
	}

	@Test
	public void return_RepositoryFoundAccount_whenRepositoryFindsIt() {
		Account account = new Account();
		long testId = 10;
		doReturn(Optional.ofNullable(account)).when(accountRepository)
			.findById(testId);

		accountService.findById(testId);
	}

}
