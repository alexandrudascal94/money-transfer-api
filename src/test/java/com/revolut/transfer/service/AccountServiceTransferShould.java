package com.revolut.transfer.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.revolut.transfer.enumeration.Currency;
import com.revolut.transfer.exception.AccountOverdraftException;
import com.revolut.transfer.exception.InvalidAmountException;
import com.revolut.transfer.formater.MoneyParser;
import com.revolut.transfer.model.Account;
import com.revolut.transfer.repository.AccountRepository;

public class AccountServiceTransferShould {

	AccountRepository accountRepository = mock(AccountRepository.class);;

	ExchangeService exchangeService = mock(ExchangeService.class);

	AccountService accountService = new AccountServiceImpl(accountRepository, exchangeService);

	@After
	public void clean() {
		accountRepository = mock(AccountRepository.class);
		exchangeService = mock(ExchangeService.class);
	}

	@Test(expected = AccountOverdraftException.class)
	public void throw_AccountOverdraftException_when_AccountFromDoesNotHaveEnoughMoney() {
		long idFrom = 10;
		long idTo = 20;
		Account accountFrom = new Account(idFrom, "test account from", Currency.EUR, MoneyParser.parse("200.00"));
		Account accountTo = new Account(idTo, "test account to", Currency.EUR, MoneyParser.parse("10.00"));
		AccountService accountServiceSpy = spy(accountService);
		doReturn(accountFrom).when(accountServiceSpy)
			.findById(idFrom);
		doReturn(accountTo).when(accountServiceSpy)
			.findById(idTo);

		accountServiceSpy.transfer(idFrom, idTo, MoneyParser.parse("1000"), Currency.EUR);
	}

	@Test
	public void subtruct_AmountFromAccountFrom_when_WhenTransferIsValidAndSameCurrency() {
		long idFrom = 10;
		long idTo = 20;
		Account accountFrom = new Account(idFrom, "test account from", Currency.EUR, MoneyParser.parse("200.00"));
		Account accountTo = new Account(idTo, "test account to", Currency.EUR, MoneyParser.parse("10.00"));
		AccountService accountServiceSpy = spy(accountService);
		doReturn(accountFrom).when(accountServiceSpy)
			.findById(idFrom);
		doReturn(accountTo).when(accountServiceSpy)
			.findById(idTo);

		accountServiceSpy.transfer(idFrom, idTo, MoneyParser.parse("10.00"), Currency.EUR);

		ArgumentCaptor<Account> argument = ArgumentCaptor.forClass(Account.class);
		verify(accountRepository, times(2)).save(argument.capture());
		assertEquals(accountFrom.getBalance()
			.subtract(MoneyParser.parse("10.00")),
				argument.getAllValues()
					.get(0)
					.getBalance());
	}

	@Test
	public void add_AmountToAccountTo_when_WhenTransferIsValidAndSameCurrency() {
		long idFrom = 10;
		long idTo = 20;
		Account accountFrom = new Account(idFrom, "test account from", Currency.EUR, MoneyParser.parse("200.00"));
		Account accountTo = new Account(idTo, "test account to", Currency.EUR, MoneyParser.parse("10.00"));
		AccountService accountServiceSpy = spy(accountService);
		doReturn(accountFrom).when(accountServiceSpy)
			.findById(idFrom);
		doReturn(accountTo).when(accountServiceSpy)
			.findById(idTo);

		accountServiceSpy.transfer(idFrom, idTo, MoneyParser.parse("10.00"), Currency.EUR);

		ArgumentCaptor<Account> argument = ArgumentCaptor.forClass(Account.class);
		verify(accountRepository, times(2)).save(argument.capture());
		assertEquals(accountTo.getBalance()
			.add(MoneyParser.parse("10.00")),
				argument.getAllValues()
					.get(1)
					.getBalance());
	}

	@Test(expected = InvalidAmountException.class)
	public void throw_InvalidAmountException_whenAmountLessThen0dot01() {
		long idFrom = 10;
		long idTo = 20;
		Account accountFrom = new Account(idFrom, "test account from", Currency.EUR, MoneyParser.parse("200.00"));
		Account accountTo = new Account(idTo, "test account to", Currency.EUR, MoneyParser.parse("10.00"));
		AccountService accountServiceSpy = spy(accountService);
		doReturn(accountFrom).when(accountServiceSpy)
			.findById(idFrom);
		doReturn(accountTo).when(accountServiceSpy)
			.findById(idTo);

		accountServiceSpy.transfer(idFrom, idTo, MoneyParser.parse("0.001"), Currency.EUR);
	}

}
