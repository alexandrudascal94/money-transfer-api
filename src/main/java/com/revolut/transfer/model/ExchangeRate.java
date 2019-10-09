package com.revolut.transfer.model;

import java.math.BigDecimal;

import com.revolut.transfer.enumeration.Currency;

public class ExchangeRate {
	
	private final Currency fromCurrency;
	private final Currency toCurrency;
	private final BigDecimal rate;
	
	public ExchangeRate(Currency fromCurrency, Currency toCurrency, BigDecimal rate) {
		this.fromCurrency = fromCurrency;
		this.toCurrency = toCurrency;
		this.rate = rate;
	}

	public Currency getFromCurrency() {
		return fromCurrency;
	}

	public Currency getToCurrency() {
		return toCurrency;
	}

	public BigDecimal getRate() {
		return rate;
	}
}
