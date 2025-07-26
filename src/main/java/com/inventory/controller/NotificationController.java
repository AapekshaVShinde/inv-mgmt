package com.inventory.controller;

import com.inventory.dto.response.NotificationResponseDto;
import com.inventory.entity.Notification;
import com.inventory.service.NotificationService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
//@Hidden
public class NotificationController {

    private final NotificationService notificationService;

    /** Get all notifications for a user */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDto>> getUserNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getNotifications(userId);
        return ResponseEntity.ok(
                notifications.stream().map(this::toDto).collect(Collectors.toList())
        );
    }

    /** Mark notification as read */
    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResponseDto> markAsRead(@PathVariable Long id) {
        Notification updated = notificationService.markAsRead(id);
        return ResponseEntity.ok(toDto(updated));
    }

    private NotificationResponseDto toDto(Notification notification) {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId(notification.getId());
        dto.setEventType(notification.getEventType());
        dto.setMessage(notification.getMessage());
        dto.setLinkToResource(notification.getLinkToResource());
        dto.setRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
}
