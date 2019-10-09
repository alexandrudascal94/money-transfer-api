package com.revolut.transfer.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.After;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.revolut.transfer.enumeration.Currency;
import com.revolut.transfer.exception.AccountOverdraftException;
import com.revolut.transfer.exception.AccountWithrowException;
import com.revolut.transfer.exception.InvalidAmountException;
import com.revolut.transfer.formater.MoneyParser;
import com.revolut.transfer.model.Account;
import com.revolut.transfer.repository.AccountRepository;

public class AccountServiceWithrowShould {

	AccountRepository accountRepository = mock(AccountRepository.class);;

	ExchangeService exchangeService = mock(ExchangeService.class);

	AccountService accountService = new AccountServiceImpl(accountRepository, exchangeService);

	@After
	public void clean() {
		accountRepository = mock(AccountRepository.class);
		exchangeService = mock(ExchangeService.class);
	}

	@Test(expected = AccountOverdraftException.class)
	public void throw_AccountOverdraftException_when_WithrowGreaterAmountThenBalance_and_BalanceSameCurrency() {
		long testId = 10;
		Account testAccount = new Account("test account", Currency.EUR, MoneyParser.parse("10.00"));
		AccountService accountServiceSpy = spy(accountService);
		doReturn(testAccount).when(accountServiceSpy)
			.findById(anyLong());
		doReturn(Optional.ofNullable(testAccount)).when(accountRepository)
			.save(any(Account.class));

		accountServiceSpy.withrow(testId, MoneyParser.parse("11.00"), Currency.EUR);
	}

	@Test(expected = AccountWithrowException.class)
	public void throw_AccountWithrowException_when_AccountNotSaved() {
		long testId = 10;
		Account testAccount = new Account("test account", Currency.EUR, MoneyParser.parse("10.00"));
		AccountService accountServiceSpy = spy(accountService);
		doReturn(testAccount).when(accountServiceSpy)
			.findById(anyLong());
		doReturn(Optional.ofNullable(null)).when(accountRepository)
			.save(any(Account.class));

		accountServiceSpy.withrow(testId, MoneyParser.parse("1.00"), Currency.EUR);
	}

	@Test
	public void return_CorrectBalance_when_withdrawnAmountIsLessThnBalance() {
		long testId = 10;
		BigDecimal initialBalance = MoneyParser.parse("10.00");
		BigDecimal withrownAmount = MoneyParser.parse("10.00");

		Account testAccount = new Account("test account", Currency.EUR, initialBalance);
		AccountService accountServiceSpy = spy(accountService);

		doReturn(testAccount).when(accountServiceSpy)
			.findById(anyLong());
		doReturn(Optional.ofNullable(testAccount)).when(accountRepository)
			.save(any(Account.class));

		ArgumentCaptor<Account> argument = ArgumentCaptor.forClass(Account.class);

		accountServiceSpy.withrow(testId, withrownAmount, Currency.EUR);
		
		verify(accountRepository).save(argument.capture());
		assertEquals(initialBalance.subtract(withrownAmount), argument.getValue()
			.getBalance());
	}

	@Test
	public void withrowExchangedAmount_when_WithrowHasDifferentCurrency() {
		long testId = 10;
		BigDecimal initialBalance = MoneyParser.parse("10.00");
		Currency accountCurrency = Currency.EUR;
		Currency withrowCurrency = Currency.GBP;
		BigDecimal exhangedWithrowAmount = MoneyParser.parse("6.67");

		Account testAccount = new Account("test account", accountCurrency, initialBalance);
		AccountService accountServiceSpy = spy(accountService);

		doReturn(exhangedWithrowAmount).when(exchangeService)
			.exchange(any(), any(), any());
		doReturn(testAccount).when(accountServiceSpy)
			.findById(anyLong());
		doReturn(Optional.ofNullable(testAccount)).when(accountRepository)
			.save(any(Account.class));

		ArgumentCaptor<Account> argument = ArgumentCaptor.forClass(Account.class);

		accountServiceSpy.withrow(testId, exhangedWithrowAmount, withrowCurrency);
		
		verify(accountRepository).save(argument.capture());
		assertEquals(initialBalance.subtract(exhangedWithrowAmount), argument.getValue()
			.getBalance());
	}

	@Test
	public void callOnce_RepositorySaveMethod() {
		long testId = 10;
		Account testAccount = new Account("test account", Currency.EUR, MoneyParser.parse("10.00"));
		AccountService accountServiceSpy = spy(accountService);
		doReturn(testAccount).when(accountServiceSpy)
			.findById(anyLong());
		doReturn(Optional.ofNullable(testAccount)).when(accountRepository)
			.save(any(Account.class));

		accountServiceSpy.withrow(testId, MoneyParser.parse("10.00"), Currency.EUR);

		verify(accountRepository, times(1)).save(any(Account.class));
	}
	
	@Test(expected = InvalidAmountException.class)
	public void throw_InvalidAmountException_whenAmountLessThen0dot01() {
		long testId = 10;
		Account testAccount = new Account("test account", Currency.EUR, MoneyParser.parse("10.00"));
		AccountService accountServiceSpy = spy(accountService);
		doReturn(testAccount).when(accountServiceSpy).findById(anyLong());
		doReturn(Optional.ofNullable(null)).when(accountRepository).save(any(Account.class));

		accountServiceSpy.withrow(testId, MoneyParser.parse("0.001"), Currency.EUR);
	}
}
