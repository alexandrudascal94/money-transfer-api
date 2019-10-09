package com.revolut.transfer.service;

import java.math.BigDecimal;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.revolut.transfer.enumeration.Currency;
import com.revolut.transfer.exception.AccountDepositException;
import com.revolut.transfer.exception.AccountNotCreatedException;
import com.revolut.transfer.exception.AccountNotFoundException;
import com.revolut.transfer.exception.AccountOverdraftException;
import com.revolut.transfer.exception.AccountWithrowException;
import com.revolut.transfer.exception.InvalidAmountException;
import com.revolut.transfer.formater.MoneyParser;
import com.revolut.transfer.model.Account;
import com.revolut.transfer.repository.AccountRepository;

public class AccountServiceImpl implements AccountService {

	private AccountRepository acccountRepository;

	private ExchangeService exchangeService;

	@Inject
	public AccountServiceImpl(AccountRepository accRepository, ExchangeService exchangeService) {
		this.acccountRepository = accRepository;
		this.exchangeService = exchangeService;
	}

	@Override
	public Account create(Account account) {
		return acccountRepository.create(account)
			.orElseThrow(() -> new AccountNotCreatedException("Account was not created."));
	}

	@Override
	@Transactional(rollbackOn = { Exception.class, RuntimeException.class })
	public Account deposit(long accountId, BigDecimal amount, Currency currency) {
		if (!isValidAmount(amount)) {
			throw new InvalidAmountException("Invalid deposit amount. must be greater then 0.01");
		}
		Account account = findById(accountId);
		account = account.addBalance(exchangeIfNeed(currency, account.getCurrency(), amount));

		return acccountRepository.save(account)
			.orElseThrow(() -> new AccountDepositException("Deposit was not performed."));

	}

	@Override
	@Transactional(rollbackOn = { Exception.class, RuntimeException.class })
	public Account withrow(long accountId, BigDecimal amount, Currency currency) {
		Account account = findById(accountId);
		BigDecimal amountToWithrow = exchangeIfNeed(currency, account.getCurrency(), amount);

		if (!isValidAmount(amount)) {
			throw new InvalidAmountException("Invalid withrow amount. must be greater then 0.01");
		}

		if (!isWithdrawable(account.getBalance(), amountToWithrow)) {
			throw new AccountOverdraftException("Not enough found for withrow");
		}
		account = account.subtractBalance(amountToWithrow);
		return acccountRepository.save(account)
			.orElseThrow(() -> new AccountWithrowException("Account was not saved"));

	}

	@Override
	@Transactional(rollbackOn = { Exception.class, RuntimeException.class })
	public void transfer(long fromAccountId, long toAccountId, BigDecimal amount, Currency currency) {
		Account fromAccount = findById(fromAccountId);
		Account toAccount = findById(toAccountId);

		if (!isValidAmount(amount)) {
			throw new InvalidAmountException("Invalid transfer amount. must be greater then 0.01");
		}

		BigDecimal amountToWithrow = exchangeIfNeed(currency, fromAccount.getCurrency(), amount);
		if (!isWithdrawable(fromAccount.getBalance(), amountToWithrow)) {
			throw new AccountOverdraftException("Not enough found for transfer");
		}
		fromAccount = fromAccount.subtractBalance(amountToWithrow);

		BigDecimal amountToDeposit = exchangeIfNeed(currency, fromAccount.getCurrency(), amount);
		toAccount = toAccount.addBalance(amountToDeposit);

		acccountRepository.save(fromAccount);
		acccountRepository.save(toAccount);

	}

	@Override
	public Account findById(long accountId) {
		return acccountRepository.findById(accountId)
			.orElseThrow(() -> new AccountNotFoundException("Account not found"));
	}

	private BigDecimal exchangeIfNeed(Currency fromCurrency, Currency toCurrency, BigDecimal amount) {
		if (fromCurrency != toCurrency) {
			return exchangeService.exchange(fromCurrency, toCurrency, amount);
		}
		return amount;
	}

	private boolean isWithdrawable(BigDecimal balance, BigDecimal amountToWithrow) {
		return balance.compareTo(amountToWithrow) >= 0;
	}

	private boolean isValidAmount(BigDecimal amount) {
		return amount.compareTo(MoneyParser.parse("0.01")) > 0;
	}
}
