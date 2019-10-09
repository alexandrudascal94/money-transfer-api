package com.revolut.transfer.repository;

import java.util.Optional;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.revolut.transfer.exception.AccountAlreadyExistsException;
import com.revolut.transfer.exception.InvalidAccountException;
import com.revolut.transfer.model.Account;

public class AccountRepositoryImpl implements AccountRepository {

	private Provider<EntityManager> entityManager;
	
	@Inject
	public AccountRepositoryImpl(Provider<EntityManager> entityManagr) {
		this.entityManager = entityManagr;
	}

	@Override
	public Optional<Account> create(Account account) {
		if (account.getId() != null) {
			throw new AccountAlreadyExistsException("Account already exists");
		} else {
			entityManager.get().getTransaction().begin();
			entityManager.get()
				.persist(account);
			entityManager.get().getTransaction().commit();
			return Optional.ofNullable(account);
		}
	}

	@Override
	public Optional<Account> findById(long id) {
		return Optional.ofNullable(entityManager.get()
			.find(Account.class, id));
	}

	@Override
	public Optional<Account> save(Account account) {
		if (account.getId() == null) {
			throw new InvalidAccountException("Account must have an id");
		} else {
			Optional<Account> newAccount = Optional.ofNullable(entityManager.get()
				.merge(account));
			return newAccount;
		}
	}
}
