package com.revolut.transfer.repository;

import java.math.BigDecimal;

import com.revolut.transfer.enumeration.Currency;

public interface ExchangeRateRepository {
	
	BigDecimal getExchangeRate(Currency fromCurrency, Currency toCurrency);
}
