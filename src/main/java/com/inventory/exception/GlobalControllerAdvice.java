package com.inventory.exception;

import com.inventory.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalControllerAdvice {

	private ResponseEntity<ErrorResponse> buildErrorResponse(String code, String message, HttpStatus status,
															 HttpServletRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(
				LocalDateTime.now(),
				code,
				message,
				status.value(),
				request.getRequestURI(),
				request.getMethod()
		);
		return new ResponseEntity<>(errorResponse, status);
	}

	@ExceptionHandler(SecurityException.class)
	public ResponseEntity<ErrorResponse> handleSecurityException(SecurityException ex, HttpServletRequest request) {
		return buildErrorResponse(ex.getErrorCode(), ex.getErrorMessage(), HttpStatus.UNAUTHORIZED, request);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
		return buildErrorResponse(ex.getErrorCode(), ex.getErrorMessage(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
		return buildErrorResponse("GENERIC_ERROR", ex.getReason(), HttpStatus.valueOf(ex.getStatusCode().value()), request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
		return buildErrorResponse("INTERNAL_ERROR", "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<ErrorResponse> handleAuthException(AuthException ex, HttpServletRequest request) {
		return buildErrorResponse(ex.getErrorCode(), ex.getErrorMessage(), HttpStatus.FORBIDDEN, request);
	}

	@ExceptionHandler(CategoryException.class)
	public ResponseEntity<ErrorResponse> handleCategoryException(CategoryException ex, HttpServletRequest request) {
		return buildErrorResponse(ex.getErrorCode(), ex.getErrorMessage(), HttpStatus.BAD_REQUEST, request);
	}

}