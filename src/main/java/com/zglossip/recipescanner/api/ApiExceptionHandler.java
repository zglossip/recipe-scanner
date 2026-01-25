package com.zglossip.recipescanner.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ApiErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
		String message = ex.getReason() != null ? ex.getReason() : "Request failed.";
		return ResponseEntity.status(ex.getStatusCode())
				.body(new ApiErrorResponse("error", message));
	}
}
