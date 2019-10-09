package com.revolut.transfer.di;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

public class TestPersistanceModule extends AbstractModule{

	@Override
	protected void configure() {
		install(new JpaPersistModule("test-database"));
		bind(PersistenceInitializer.class).to(TestPersistenceInitializer.class);
	}	
}