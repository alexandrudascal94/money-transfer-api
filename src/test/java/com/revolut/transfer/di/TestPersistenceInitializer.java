package com.revolut.transfer.di;

import com.google.inject.Inject;
import com.google.inject.persist.PersistService;

public class TestPersistenceInitializer implements PersistenceInitializer{

	private PersistService service;

	@Inject
	TestPersistenceInitializer(PersistService service) {
		this.service = service;
	}

	@Override
	public void start() {
		service.start();
	}
}
