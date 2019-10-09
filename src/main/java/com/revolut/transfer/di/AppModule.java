package com.revolut.transfer.di;

import com.google.inject.AbstractModule;
import com.revolut.transfer.controller.AccountController;
import com.revolut.transfer.repository.AccountRepository;
import com.revolut.transfer.repository.AccountRepositoryImpl;
import com.revolut.transfer.repository.ExchangeRateRepository;
import com.revolut.transfer.repository.InMemoryExchangeRateRepository;
import com.revolut.transfer.service.AccountService;
import com.revolut.transfer.service.AccountServiceImpl;
import com.revolut.transfer.service.ExchangeService;
import com.revolut.transfer.service.ExchangeServiceImpl;

public class AppModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AccountController.class);
		bind(AccountService.class).to(AccountServiceImpl.class);
		bind(ExchangeService.class).to(ExchangeServiceImpl.class);
		bind(AccountRepository.class).to(AccountRepositoryImpl.class);
		bind(ExchangeService.class).to(ExchangeServiceImpl.class);
		bind(ExchangeRateRepository.class).to(InMemoryExchangeRateRepository.class);
	}
}
