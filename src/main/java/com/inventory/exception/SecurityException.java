package com.inventory.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.Date;

@Getter
public class SecurityException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;

    public SecurityException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}