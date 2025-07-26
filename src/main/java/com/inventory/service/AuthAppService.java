package com.inventory.service;

import com.inventory.entity.PasswordResetToken;
import com.inventory.entity.User;
import com.inventory.exception.AuthException;
import com.inventory.repo.PasswordResetTokenRepository;
import com.inventory.repo.UserRepository;
import com.inventory.security.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthAppService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService jwtService; // existing JWT helper
    private final PasswordResetTokenRepository tokenRepository;
    private final MailService mailService;

    @Value("${ui-base-url}")
    private String baseUrl;

    @Value("${token-expiry}")
    private Long tokenExpiryInMinutes;

    public String login(String emailOrUsername, String password) {
        User user = userRepository.findByEmailAndActiveTrue(emailOrUsername)
                .or(() -> userRepository.findByUsernameAndActiveTrue(emailOrUsername))
                .orElseThrow(() -> new AuthException("AUTH001", "Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException("AUTH001", "Invalid credentials");
        }

        return jwtService.generateToken(user.getEmail());
    }

    public String generatePasswordResetToken(String email) {
        User user = userRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new AuthException("AUTH002", "User with this email does not exist or is inactive"));

        // Create token
        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(tokenExpiryInMinutes));
        tokenRepository.save(resetToken);

        // In real app: send email with reset link
        String resetLink = baseUrl + "/reset-password?token=" + token;
        mailService.sendPasswordResetEmail(user.getEmail(), resetLink);
        log.info("Reset link: {}", resetLink);
        return token;
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new AuthException("AUTH003", "Invalid or expired reset token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new AuthException("AUTH004", "Reset token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken); // Invalidate token
    }
}