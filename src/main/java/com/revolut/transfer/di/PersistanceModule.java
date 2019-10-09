package com.revolut.transfer.di;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

public class PersistanceModule extends AbstractModule{

	@Override
	protected void configure() {
		install(new JpaPersistModule("revolut"));
		bind(PersistenceInitializer.class).to(PersistenceInitializerImpl.class);
	}	
}
