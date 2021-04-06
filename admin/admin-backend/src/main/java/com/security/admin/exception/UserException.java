package com.security.admin.exception;

public class UserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String causeField;
	private String causeMessage;
	
	public UserException() {
	}

	public UserException(String message, String causeField, String causeMessage) {
		super(message);
		this.causeField = causeField;
		this.causeMessage = causeMessage;
	}

	public String getCauseField() {
		return causeField;
	}

	public void setCauseField(String causeField) {
		this.causeField = causeField;
	}

	public String getCauseMessage() {
		return causeMessage;
	}

	public void setCauseMessage(String causeMessage) {
		this.causeMessage = causeMessage;
	}

}
