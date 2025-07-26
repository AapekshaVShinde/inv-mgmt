package com.inventory.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String emailOrUsername;
    private String password;
}
