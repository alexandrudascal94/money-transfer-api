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
import com.revolut.transfer.exception.AccountDepositException;
import com.revolut.transfer.exception.InvalidAmountException;
import com.revolut.transfer.formater.MoneyParser;
import com.revolut.transfer.model.Account;
import com.revolut.transfer.repository.AccountRepository;

public class AccountServiceDepositShould {

	AccountRepository accountRepository = mock(AccountRepository.class);;

	ExchangeService exchangeService = mock(ExchangeService.class);

	AccountService accountService = new AccountServiceImpl(accountRepository, exchangeService);
	
	@After
	public void clean() {
		accountRepository = mock(AccountRepository.class);
		exchangeService = mock(ExchangeService.class);
	}

	@Test(expected = AccountDepositException.class)
	public void throw_AccountDepositException_whenAccountNotSaved() {
		long testId = 10;
		Account testAccount = new Account("test account", Currency.EUR, MoneyParser.parse("10.00"));
		AccountService accountServiceSpy = spy(accountService);
		doReturn(testAccount).when(accountServiceSpy).findById(anyLong());
		doReturn(Optional.ofNullable(null)).when(accountRepository).save(any(Account.class));

		accountServiceSpy.deposit(testId, MoneyParser.parse("10.00"), Currency.EUR);
	}
	
	@Test(expected = InvalidAmountException.class)
	public void throw_InvalidAmountException_whenAmountLessThen0dot01() {
		long testId = 10;
		Account testAccount = new Account("test account", Currency.EUR, MoneyParser.parse("10.00"));
		AccountService accountServiceSpy = spy(accountService);
		doReturn(testAccount).when(accountServiceSpy).findById(anyLong());
		doReturn(Optional.ofNullable(null)).when(accountRepository).save(any(Account.class));

		accountServiceSpy.deposit(testId, MoneyParser.parse("0.001"), Currency.EUR);
	}
	
	@Test
	public void callOnce_RepositorySaveMethod() {
		long testId = 10;
		Account testAccount = new Account("test account", Currency.EUR, MoneyParser.parse("10.00"));
		AccountService accountServiceSpy = spy(accountService);
		doReturn(testAccount).when(accountServiceSpy).findById(anyLong());
		doReturn(Optional.ofNullable(testAccount)).when(accountRepository).save(any(Account.class));

		accountServiceSpy.deposit(testId, MoneyParser.parse("10.00"), Currency.EUR);
		
		verify(accountRepository, times(1)).save(any(Account.class));
	}
	
	@Test
	public void addSameAmount_when_DepositHasSameCurrency() {
		long testId = 10;
		BigDecimal initialBalance = MoneyParser.parse("1");
		Currency currency = Currency.EUR;
		
		Account testAccount = new Account("test account", currency, initialBalance);
		AccountService accountServiceSpy = spy(accountService);
		doReturn(testAccount).when(accountServiceSpy).findById(anyLong());
		doReturn(Optional.ofNullable(testAccount)).when(accountRepository).save(any(Account.class));
		
		ArgumentCaptor<Account> argument = ArgumentCaptor.forClass(Account.class);
		accountServiceSpy.deposit(testId, MoneyParser.parse("10.00"), currency);	
		
		verify(accountRepository).save(argument.capture());
		assertEquals(initialBalance.add(MoneyParser.parse("10.00")), argument.getValue().getBalance());
	}
	
	@Test
	public void addExchangedAmount_when_DepositHasDifferentCurrency() {
		long testId = 10;
		BigDecimal initialBalance = MoneyParser.parse("10.00");
		Currency accountCurrency = Currency.EUR;
		Currency depositCurrency = Currency.GBP;
		BigDecimal exhangedDepositAmount = MoneyParser.parse("10.00");
		
		Account testAccount = new Account("test account", accountCurrency, initialBalance);
		AccountService accountServiceSpy = spy(accountService);
		
		doReturn(exhangedDepositAmount).when(exchangeService).exchange(any(), any(), any());
		doReturn(testAccount).when(accountServiceSpy).findById(anyLong());
		doReturn(Optional.ofNullable(testAccount)).when(accountRepository).save(any(Account.class));
		
		ArgumentCaptor<Account> argument = ArgumentCaptor.forClass(Account.class);

		accountServiceSpy.deposit(testId, exhangedDepositAmount, depositCurrency);
						
		verify(accountRepository).save(argument.capture());
		assertEquals(initialBalance.add(MoneyParser.parse("10.00")), argument.getValue().getBalance());
	}

}
