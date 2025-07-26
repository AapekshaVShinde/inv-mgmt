package com.inventory.dto.response;

import com.inventory.enums.RoleType;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserResponseDTO {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String phone;
    private String position;
    private LocalDate dateOfJoining;
    private Long storeId;
    private Long departmentId;
    private RoleType role;
    private boolean active;
}
