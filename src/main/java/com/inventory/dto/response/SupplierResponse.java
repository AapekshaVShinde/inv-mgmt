package com.inventory.dto.response;

import lombok.Data;

@Data
public class SupplierResponse {
    private Long id;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private boolean active;
}
