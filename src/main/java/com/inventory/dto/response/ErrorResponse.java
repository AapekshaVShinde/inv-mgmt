package com.inventory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String errorCode;
    private String message;
    private int status;
    private String path;
    private String method;
}
