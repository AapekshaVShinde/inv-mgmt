package com.inventory.dto.request;

import lombok.Data;

@Data
public class StoreRequest {
    private String name;
    private String location;
    private String managerName;
    private String contactNumber;
}

