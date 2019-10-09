package com.revolut.transfer.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Singleton;
import com.revolut.transfer.enumeration.Currency;
import com.revolut.transfer.exception.ExchangeRateNotFoundException;
import com.revolut.transfer.formater.MoneyParser;
import com.revolut.transfer.model.ExchangeRate;

@Singleton
public class InMemoryExchangeRateRepository implements ExchangeRateRepository {

	private List<ExchangeRate> rates = new ArrayList<ExchangeRate>();

	public InMemoryExchangeRateRepository(List<ExchangeRate> reates) {
		rates = new ArrayList<ExchangeRate>();
		rates.addAll(reates);
	}
	
	public InMemoryExchangeRateRepository() {
		rates.add(new ExchangeRate(Currency.EUR, Currency.USD, MoneyParser.parse("1.10")));
		rates.add(new ExchangeRate(Currency.EUR, Currency.GBP, MoneyParser.parse("0.89")));
		rates.add(new ExchangeRate(Currency.USD, Currency.EUR, MoneyParser.parse("0.91")));
		rates.add(new ExchangeRate(Currency.USD, Currency.GBP, MoneyParser.parse("0.81")));
		rates.add(new ExchangeRate(Currency.GBP, Currency.EUR, MoneyParser.parse("1.12")));
		rates.add(new ExchangeRate(Currency.GBP, Currency.USD, MoneyParser.parse("1.23")));
	}

	@Override
	public BigDecimal getExchangeRate(Currency fromCurrency, Currency toCurrency) {

		 ExchangeRate rate = rates.stream()
				.filter(r -> r.getFromCurrency() == fromCurrency 
							&& r.getToCurrency() == toCurrency)
				.findFirst()
				.orElseThrow(() -> new ExchangeRateNotFoundException("Rate not found"));
		 
		 return rate.getRate();
	}
}
