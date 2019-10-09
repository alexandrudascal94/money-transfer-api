package com.revolut.transfer.repository;

import java.util.Optional;

import com.revolut.transfer.model.Account;

public interface AccountRepository {
	Optional<Account> create(Account account);
	Optional<Account> save(Account account);
	Optional<Account> findById(long id);
}
