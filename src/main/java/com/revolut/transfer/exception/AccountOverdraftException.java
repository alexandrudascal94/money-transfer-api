package com.revolut.transfer.exception;

public class AccountOverdraftException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5870104051597126200L;

	public AccountOverdraftException(String message) {
		super(message);
	}
}
