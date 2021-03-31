package com.security.hospital.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String causeField;
	private String causeMessage;
	
	public UserException(String message, String causeField, String causeMessage) {
		super(message);
		this.causeField = causeField;
		this.causeMessage = causeMessage;
	}

}
