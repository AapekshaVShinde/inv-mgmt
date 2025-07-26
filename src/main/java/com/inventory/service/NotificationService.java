package com.inventory.service;

import com.inventory.entity.Notification;
import com.inventory.entity.User;
import com.inventory.exception.NotificationException;
import com.inventory.repo.NotificationRepository;
import com.inventory.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    /** Get all notifications for a user */
    public List<Notification> getNotifications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotificationException("NOTIF001", "User not found", HttpStatus.NOT_FOUND));
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    /** Mark a notification as read */
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationException("NOTIF002", "Notification not found", HttpStatus.NOT_FOUND));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    /** Create new notification */
    public Notification createNotification(User user, String eventType, String message, String linkToResource) {
        Notification notification = Notification.builder()
                .user(user)
                .eventType(eventType)
                .message(message)
                .linkToResource(linkToResource)
                .build();
        return notificationRepository.save(notification);
    }
}
