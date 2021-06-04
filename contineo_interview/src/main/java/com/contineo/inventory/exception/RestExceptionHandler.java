package com.contineo.inventory.exception;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.contineo.inventory.controller.JsonMessage;

@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<JsonMessage> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
		List<String> errors = new ArrayList<>();
		for(ConstraintViolation<?> violation:ex.getConstraintViolations()) {
			errors.add(violation.getMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonMessage(false, errors));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<JsonMessage> handleException(Exception ex, WebRequest request) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JsonMessage(false, ex.getMessage()));
	} 
}
