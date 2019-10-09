package com.revolut.transfer.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Test;

import com.google.inject.Provider;
import com.revolut.transfer.di.TestEntityManagerProvider;
import com.revolut.transfer.enumeration.Currency;
import com.revolut.transfer.formater.MoneyParser;
import com.revolut.transfer.model.Account;

public class AccountRepositoryFindByIdShould {
	private Provider<EntityManager> entityManager = new TestEntityManagerProvider();

	private AccountRepository accountRepository = new AccountRepositoryImpl(entityManager);

	@After
	public void clean() {
		entityManager.get()
			.clear();
	}

	@Test
	public void bePresent_when_AccountExists() {
		Account account = accountRepository.create(new Account("test name", Currency.EUR, MoneyParser.parse("1.00")))
			.get();
		entityManager.get()
			.clear();
		assertTrue(accountRepository.findById(account.getId())
			.isPresent());
	}

	@Test
	public void notBePresent_when_AccountDoesNotExist() {
		long testId = 123L;
		assertFalse(accountRepository.findById(testId)
			.isPresent());
	}
}
