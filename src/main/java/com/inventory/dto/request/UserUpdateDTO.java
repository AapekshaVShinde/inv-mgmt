package com.inventory.dto.request;

import com.inventory.enums.RoleType;
import lombok.Data;

@Data
public class UserUpdateDTO {
    private String fullName;
    private String phone;
    private String position;
    private boolean active;
    private Long storeId;
    private Long departmentId;
    private RoleType role;
    private String password; // optional password update
}
