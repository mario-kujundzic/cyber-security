package com.security.admin.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionHandlers {

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorMessage> loginExceptionHandler(UserException ex, WebRequest request) {
		ErrorMessage message = new ErrorMessage(ex.getMessage());
		message.getErrors().put(ex.getCauseField(), ex.getCauseMessage());
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(OftenUsedPasswordException.class)
	public ResponseEntity<ErrorMessage> OftenUsedPasswordExceptionHandler(OftenUsedPasswordException ex, WebRequest request) {
		ErrorMessage message = new ErrorMessage(ex.getMessage());
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}
		
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
		ErrorMessage message = new ErrorMessage(ex.getMessage());
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

}
