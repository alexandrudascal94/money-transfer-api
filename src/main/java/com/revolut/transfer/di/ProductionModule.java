package com.revolut.transfer.di;

import com.google.inject.AbstractModule;

public class ProductionModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new AppModule());
		install(new PersistanceModule());
	}
}
