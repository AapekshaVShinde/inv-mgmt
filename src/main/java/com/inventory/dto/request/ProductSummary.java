package com.inventory.dto.request;

import lombok.Data;

@Data
public class ProductSummary {
    private Long id;
    private String name;
    private Integer quantity;
}
