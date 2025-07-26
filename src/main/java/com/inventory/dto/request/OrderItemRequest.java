package com.inventory.dto.request;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long productId;
    private Long categoryId;
    private Integer quantity;
}
