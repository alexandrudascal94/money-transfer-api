package com.revolut.transfer.dto;

public class RequestErrorRespose {
	public int code;
	public String message;
	public String request;
	
	public RequestErrorRespose(int code, String message, String request) {
		this.code = code;
		this.message = message;
		this.request = request;
	}
	
	
}
