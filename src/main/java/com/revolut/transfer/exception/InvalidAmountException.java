package com.revolut.transfer.exception;

public class InvalidAmountException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3672158722675032862L;

	public InvalidAmountException(String message){
		super(message);
	}
}
