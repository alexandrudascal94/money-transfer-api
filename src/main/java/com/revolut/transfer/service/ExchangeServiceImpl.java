package com.revolut.transfer.service;

import java.math.BigDecimal;

import com.google.inject.Inject;
import com.revolut.transfer.enumeration.Currency;
import com.revolut.transfer.repository.ExchangeRateRepository;

public class ExchangeServiceImpl implements ExchangeService {

	@Inject
	private ExchangeRateRepository exchangeRepository;

	@Override
	public BigDecimal exchange(Currency fromCurrency, Currency toCurrency, BigDecimal amount) {
		return amount.multiply(exchangeRepository.getExchangeRate(fromCurrency, toCurrency));
	}
}
