package com.revolut.transfer.di;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;

@Singleton
public class PersistenceInitializerImpl implements PersistenceInitializer {

	private PersistService service;

	@Inject
	PersistenceInitializerImpl(PersistService service) {
		this.service = service;
	}

	@Override
	public void start() {
		service.start();
	}
}
