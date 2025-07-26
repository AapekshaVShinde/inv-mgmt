package com.inventory.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private Long storeId;
    private Long userId;
    private List<OrderItemRequest> items;
}
