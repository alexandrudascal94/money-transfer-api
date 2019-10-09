package com.revolut.transfer.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.After;
import org.junit.Test;

import com.revolut.transfer.enumeration.Currency;
import com.revolut.transfer.exception.AccountNotCreatedException;
import com.revolut.transfer.model.Account;
import com.revolut.transfer.repository.AccountRepository;

public class AccountServiceCreateShould {

	AccountRepository accountRepository = mock(AccountRepository.class);

	ExchangeService exchangeService = mock(ExchangeService.class);

	AccountService accountService = new AccountServiceImpl(accountRepository, exchangeService);

	@After
	public void clean() {
		accountRepository = mock(AccountRepository.class);
		exchangeService = mock(ExchangeService.class);
	}

	@Test
	public void callOnce_RepositoryCreateMethod() {
		Account account = new Account("savings", Currency.EUR, BigDecimal.TEN);
		doReturn(Optional.ofNullable(account)).when(accountRepository)
			.create(any(Account.class));

		accountService.create(account);

		verify(accountRepository, times(1)).create(account);
	}

	@Test(expected = AccountNotCreatedException.class)
	public void throw_AccountNotCreatedException_when_repositoryReturnOptionalWithNull() {
		Account account = new Account("savings", Currency.EUR, BigDecimal.TEN);
		doReturn(Optional.ofNullable(null)).when(accountRepository)
			.create(account);

		accountService.create(account);
	}
}
