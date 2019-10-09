package com.revolut.transfer.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Provider;
import com.revolut.transfer.di.TestEntityManagerProvider;
import com.revolut.transfer.enumeration.Currency;
import com.revolut.transfer.exception.InvalidAccountException;
import com.revolut.transfer.formater.MoneyParser;
import com.revolut.transfer.model.Account;

public class AccountRepositorySaveMethodShould {

	private Provider<EntityManager> entityManager = new TestEntityManagerProvider();
	private AccountRepository accountRepository = new AccountRepositoryImpl(entityManager);

	@Before
	public void clean() {
		entityManager.get()
			.clear();
	}

	@Test
	public void persistChanges_when_AccountIsValid() {
		Account account = new Account("test name", Currency.EUR, MoneyParser.parse("1000.00"));
		accountRepository.create(account);
		Account newAccount = new Account(account.getId(), "test name", Currency.EUR, MoneyParser.parse("10.00"));

		Account savedAccount = accountRepository.save(newAccount)
			.get();

		assertEquals(newAccount.getId(), savedAccount.getId());
		assertEquals(savedAccount.getBalance(), newAccount.getBalance());
	}

	@Test
	public void accountIsPresent_when_AccountIsValid() {
		Account account = new Account("test name", Currency.EUR, MoneyParser.parse("1000.00"));
		accountRepository.create(account);
		Account newAccount = new Account(account.getId(), "test name", Currency.EUR, MoneyParser.parse("10.00"));

		assertTrue(accountRepository.save(newAccount)
			.isPresent());
	}

	@Test(expected = InvalidAccountException.class)
	public void throw_InvalidAccountException_when_AccountDoesNotHaveId() {
		Account account = new Account("test name", Currency.EUR, MoneyParser.parse("1000.00"));

		accountRepository.save(account);
	}
}
