package com.inventory.dto.request;

import lombok.Data;

@Data
public class NotificationRequestDto {
    private Long userId;
    private String eventType;
    private String message;
    private String linkToResource;
}
