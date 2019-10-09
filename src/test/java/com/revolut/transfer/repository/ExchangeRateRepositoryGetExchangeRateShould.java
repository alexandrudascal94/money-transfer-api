package com.revolut.transfer.repository;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Test;

import com.revolut.transfer.enumeration.Currency;
import com.revolut.transfer.exception.ExchangeRateNotFoundException;
import com.revolut.transfer.formater.MoneyParser;
import com.revolut.transfer.model.ExchangeRate;

public class ExchangeRateRepositoryGetExchangeRateShould {

	private ExchangeRateRepository rateReposiotry = new InMemoryExchangeRateRepository(
			Arrays.asList(new ExchangeRate(Currency.EUR, Currency.USD, MoneyParser.parse("1.10")),
					new ExchangeRate(Currency.EUR, Currency.GBP, MoneyParser.parse("0.89")),
					new ExchangeRate(Currency.USD, Currency.GBP, MoneyParser.parse("0.81"))));

	@Test(expected = ExchangeRateNotFoundException.class)
	public void throw_ExchangeRateNotFoundException_whenRateisNotFound() {
		rateReposiotry.getExchangeRate(Currency.USD, Currency.EUR);
	}

	@Test
	public void get_CorrectRate_whenRateExists() {
		BigDecimal expectedRate = MoneyParser.parse("0.89");
		assertEquals(rateReposiotry.getExchangeRate(Currency.EUR, Currency.GBP), expectedRate);
	}

}
