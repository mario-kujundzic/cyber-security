package com.security.hospital.exceptions;

public class MaliciousIPAddressException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public MaliciousIPAddressException() {
	}

	public MaliciousIPAddressException(String message) {
		super(message);
	}
}
