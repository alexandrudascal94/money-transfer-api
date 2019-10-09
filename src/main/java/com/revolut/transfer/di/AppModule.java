package com.revolut.transfer.di;

import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new BusinessModele());
		install(new PersistanceModule());
	}
}
