package com.inventory.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String eventType; // e.g., "ORDER_PLACED", "PRODUCT_ADDED"

    @Column(nullable = false)
    private String message;

    private String linkToResource; // e.g., URL to order/product page

    @Builder.Default
    private boolean isRead = false;
}
