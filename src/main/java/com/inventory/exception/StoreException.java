package com.inventory.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class StoreException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    public StoreException(String errorCode, String errorMessage, HttpStatus status) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }
}
