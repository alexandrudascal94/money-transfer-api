package com.revolut.transfer.exception;

public class AccountNotFoundException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5495090007824582487L;

	public AccountNotFoundException(String message) {
		super(message);
	}

}
