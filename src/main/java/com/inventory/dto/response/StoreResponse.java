package com.inventory.dto.response;

import com.inventory.dto.request.ProductSummary;
import lombok.Data;

import java.util.List;

@Data
public class StoreResponse {
    private Long id;
    private String name;
    private String location;
    private String managerName;
    private String contactNumber;
    private boolean active;
    private List<ProductSummary> products; // Summary view of products in store
}
