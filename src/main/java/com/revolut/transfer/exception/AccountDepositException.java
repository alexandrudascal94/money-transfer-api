package com.revolut.transfer.exception;

public class AccountDepositException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4004090875010289755L;

	public AccountDepositException(String message) {
		super(message);
	}
}