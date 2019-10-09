package com.revolut.transfer.exception;

public class ExchangeRateNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 913404797107994619L;

	public ExchangeRateNotFoundException(String message) {
		super(message);
	}
}
