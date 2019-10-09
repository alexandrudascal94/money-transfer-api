package com.revolut.transfer.exception;

public class AccountNotCreatedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1304884617863645072L;

	public AccountNotCreatedException(String messaage) {
		super(messaage);
	}
}
