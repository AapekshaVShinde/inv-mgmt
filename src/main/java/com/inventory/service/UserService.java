package com.inventory.service;

import com.inventory.entity.*;
import com.inventory.enums.RoleType;
import com.inventory.exception.UserException;
import com.inventory.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository tokenRepository;
    private final MailService mailService;

    @Value("${ui-base-url}")
    private String baseUrl;

    /** Get all users with pagination */
    public Page<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findByActiveTrue(pageable);
    }

    /** Get user by ID */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException("USER001", "User not found", HttpStatus.NOT_FOUND));
    }

    /** Create new user (admin-created) */
    public User createUser(String fullName, String username, String email,
                           String phone, String position, LocalDate doj,
                           Long storeId, Long departmentId, RoleType role) {

        validateUniqueFields(username, email);

        Store store = null;
        Department department = null;

        if (role == RoleType.ROLE_EMPLOYEE) {
            store = getStoreIfPresent(storeId);
            department = getDepartmentIfPresent(departmentId);
        }

        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(null); // No password yet
        user.setPhone(phone);
        user.setRole(role);
        user.setActive(true);

        if (role == RoleType.ROLE_EMPLOYEE) {
            user.setPosition(position);
            user.setDateOfJoining(doj != null ? doj : LocalDate.now());
            user.setStore(store);
            user.setDepartment(department);
        }

        User savedUser = userRepository.save(user);

        // Generate registration token
        String token = UUID.randomUUID().toString();
        PasswordResetToken registrationToken = new PasswordResetToken();
        registrationToken.setToken(token);
        registrationToken.setUser(savedUser);
        registrationToken.setExpiryDate(LocalDateTime.now().plusDays(1)); // 24 hours validity
        tokenRepository.save(registrationToken);

        // Send email with registration link
        String registrationLink = baseUrl + "/set-password?token=" + token;
        mailService.sendRegistrationEmail(email, registrationLink);

        return savedUser;
    }


    /** Update existing user */
    public User updateUser(Long id, String fullName, String phone, String position,
                           boolean active, Long storeId, Long departmentId, RoleType role, String password) {
        User user = getUserById(id);

        user.setFullName(fullName);
        user.setPhone(phone);
        user.setActive(active);
        user.setRole(role);

        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        if (role == RoleType.ROLE_EMPLOYEE) {
            user.setPosition(position);
            user.setStore(getStoreIfPresent(storeId));
            user.setDepartment(getDepartmentIfPresent(departmentId));
            if (user.getDateOfJoining() == null) {
                user.setDateOfJoining(LocalDate.now());
            }
        } else {
            // Clear employee-specific fields if changed to admin
            user.setPosition(null);
            user.setDateOfJoining(null);
            user.setStore(null);
            user.setDepartment(null);
        }

        return userRepository.save(user);
    }

    /** Soft delete user */
    public void deleteUser(Long id) {
        User user = getUserById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    /** === Helpers === */
    private void validateUniqueFields(String username, String email) {
        if (userRepository.findByEmailAndActiveTrue(email).isPresent()) {
            throw new UserException("USER002", "Email already exists", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.findByUsernameAndActiveTrue(username).isPresent()) {
            throw new UserException("USER003", "Username already exists", HttpStatus.BAD_REQUEST);
        }
    }

    private Store getStoreIfPresent(Long storeId) {
        if (storeId == null) {
            throw new UserException("USER006", "Store is required for employees", HttpStatus.BAD_REQUEST);
        }
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new UserException("USER004", "Store not found", HttpStatus.NOT_FOUND));
    }

    private Department getDepartmentIfPresent(Long departmentId) {
        if (departmentId == null) {
            throw new UserException("USER007", "Department is required for employees", HttpStatus.BAD_REQUEST);
        }
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new UserException("USER005", "Department not found", HttpStatus.NOT_FOUND));
    }
}
