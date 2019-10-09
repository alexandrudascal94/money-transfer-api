package com.revolut.transfer.service;

import java.math.BigDecimal;

import com.revolut.transfer.enumeration.Currency;

public interface ExchangeService {
	public BigDecimal exchange(Currency fromCurrency, Currency toCurrency, BigDecimal amount);
}
