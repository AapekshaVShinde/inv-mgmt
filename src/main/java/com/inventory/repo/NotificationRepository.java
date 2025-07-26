package com.inventory.repo;

import com.inventory.entity.Notification;
import com.inventory.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserAndIsReadFalse(User user);
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
}
