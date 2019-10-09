package com.revolut.transfer.di;

import com.google.inject.AbstractModule;
import com.revolut.transfer.di.AppModule;

public class TestModule extends AbstractModule {
	
	@Override
	protected void configure() {
		install(new AppModule());
		install(new TestPersistanceModule());
	}	
}