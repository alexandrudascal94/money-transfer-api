package com.revolut.transfer.di;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.inject.Provider;

public class TestEntityManagerProvider implements Provider<EntityManager> {

	private static EntityManager entityManagerInstance;

	static {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("test-database");
		entityManagerInstance = entityManagerFactory.createEntityManager();
	}

	@Override
	public EntityManager get() {
		return entityManagerInstance;
	}
}
