package com.revolut.transfer.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.google.inject.Provider;
import com.revolut.transfer.di.TestEntityManagerProvider;
import com.revolut.transfer.enumeration.Currency;
import com.revolut.transfer.exception.AccountAlreadyExistsException;
import com.revolut.transfer.formater.MoneyParser;
import com.revolut.transfer.model.Account;

public class AccountRepositoryCreateMethodShould {

	private Provider<EntityManager> entityManager = new TestEntityManagerProvider();
	private AccountRepository accountRepository = new AccountRepositoryImpl(entityManager);

	@Test
	public void accountBePresent_when_AccountIsValid() {
		Account account = new Account("test name", Currency.EUR, MoneyParser.parse("1000.00"));
		assertTrue(accountRepository.create(account)
			.isPresent());
	}

	@Test(expected = AccountAlreadyExistsException.class)
	public void throw_AccountAlreadyExistsException_when_AccountHasId() {
		Account account = new Account(12L, "test name", Currency.EUR, MoneyParser.parse("1000.00"));
		accountRepository.create(account);
	}

	@Test
	public void have_Id_when_when_AccountIsValid() {
		Account account = new Account("test name", Currency.EUR, MoneyParser.parse("1000.00"));
		assertNotNull(accountRepository.create(account)
			.get()
			.getId());
	}

	@Test
	public void findInDataBase_when_callFindById() {
		Account account = new Account("test name", Currency.EUR, MoneyParser.parse("1000.00"));
		Account savedAccount = accountRepository.create(account)
			.get();

		Account foundAccount = entityManager.get()
			.find(Account.class, savedAccount.getId());

		assertSame(savedAccount, foundAccount);
	}
}
