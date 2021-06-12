package com.security.hospital.exceptions;

public class OftenUsedPasswordException extends Exception {
	
private static final long serialVersionUID = 1L;
	
	
	public OftenUsedPasswordException() {
	}

	public OftenUsedPasswordException(String message) {
		super(message);
	}

}
