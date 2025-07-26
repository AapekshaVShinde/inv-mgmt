package com.inventory.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {
    private Long productId;
    private String productName;
    private String categoryName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
}
