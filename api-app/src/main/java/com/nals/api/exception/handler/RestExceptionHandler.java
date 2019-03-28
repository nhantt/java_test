package com.nals.api.exception.handler;


import java.time.LocalDateTime;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nals.api.dto.response.ExceptionResponse;
import com.nals.api.exception.EntityNotFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<ExceptionResponse> handleEntityNotFound(EntityNotFoundException ex) {
		ExceptionResponse response = new ExceptionResponse();
		response.setMessage(ex.getMessage());
		response.setTimestamp(LocalDateTime.now());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
}
