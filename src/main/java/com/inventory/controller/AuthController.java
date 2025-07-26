package com.inventory.controller;

import com.inventory.dto.request.LoginRequest;
import com.inventory.dto.response.JwtResponse;
import com.inventory.service.AuthAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthAppService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getEmailOrUsername(), request.getPassword());
        return ResponseEntity.ok(new JwtResponse(token, request.getEmailOrUsername()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("userEmail") String userEmail) {
        String token = authService.generatePasswordResetToken(userEmail);
        return ResponseEntity.ok("Password reset link has been sent to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam(value = "token", required = true) String token, @RequestParam(required = true) String password) {
        authService.resetPassword(token, password);
        return ResponseEntity.ok("Password has been reset successfully.");
    }

    @PostMapping("/set-password")
    public ResponseEntity<String> setPassword(@RequestParam(value = "token", required = true) String token, @RequestParam(required = true) String password) {
        authService.resetPassword(token, password);
        return ResponseEntity.ok("Password has been set successfully.");
    }
}
