package com.inventory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request - Inventory Management");
        message.setText("""
                Hello,
                
                You requested a password reset. Please click the link below to reset your password:
                
                %s
                
                This link will expire in 15 minutes.
                
                If you didn’t request this, you can ignore this email.
                
                Regards,
                Inventory Management Team
                """.formatted(resetLink));
        mailSender.send(message);
        log.info("Password reset email sent to {}", to);
    }

    public void sendRegistrationEmail(String to, String registrationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Complete Your Registration");
        message.setText("Hello,\n\nPlease set your password using the following link:\n" + registrationLink + "\n\nThis link will expire in 24 hours.");
        mailSender.send(message);
    }
}
