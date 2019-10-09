package com.revolut.transfer.exception;

public class InvalidAccountException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3370866663470512634L;

	public InvalidAccountException(String message) {
		super(message);
	}
}
