package com.security.admin.exception;

public class OftenUsedPasswordException extends Exception {
	
private static final long serialVersionUID = 1L;
	
	
	public OftenUsedPasswordException() {
	}

	public OftenUsedPasswordException(String message) {
		super(message);
	}

}
