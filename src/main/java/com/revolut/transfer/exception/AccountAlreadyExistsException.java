package com.revolut.transfer.exception;

public class AccountAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5716985437191092264L;

	public AccountAlreadyExistsException(String message) {
		super(message);
	}

}
