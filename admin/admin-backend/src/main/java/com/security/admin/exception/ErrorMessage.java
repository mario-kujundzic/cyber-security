package com.security.admin.exception;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessage {
	private String message;
	private String jwt;
	private Map<String, String> errors;

	public ErrorMessage() {

	}

	public ErrorMessage(String message) {
		this.message = message;
		this.errors = new HashMap<>();
	}

	public ErrorMessage(String message, String jwt) {
		this.message = message;
		this.jwt = jwt;
		this.errors = new HashMap<>();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
}
