package com.inventory.dto.request;

import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private String description;
    private boolean active = true;
}
