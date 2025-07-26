package com.inventory.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponseDto {
    private Long id;
    private String eventType;
    private String message;
    private String linkToResource;
    private boolean isRead;
    private LocalDateTime createdAt;
}
