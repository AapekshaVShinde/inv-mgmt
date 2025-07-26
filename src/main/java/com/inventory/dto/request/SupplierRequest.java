package com.inventory.dto.request;

import lombok.Data;

@Data
public class SupplierRequest {
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
}

