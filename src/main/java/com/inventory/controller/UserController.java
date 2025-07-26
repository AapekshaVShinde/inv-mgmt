package com.inventory.controller;

import com.inventory.dto.request.UserRequestDTO;
import com.inventory.dto.request.UserUpdateDTO;
import com.inventory.dto.response.UserResponseDTO;
import com.inventory.entity.User;
import com.inventory.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
//@Hidden
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        Page<User> users = userService.getAllUsers(page, size);
        Page<UserResponseDTO> response = users.map(this::toResponseDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponseDTO(userService.getUserById(id)));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO request) {
        User createdUser = userService.createUser(
                request.getFullName(), request.getUsername(), request.getEmail(),
                request.getPhone(), request.getPosition(), request.getDateOfJoining(),
                request.getStoreId(), request.getDepartmentId(), request.getRole()
        );
        return ResponseEntity.ok(toResponseDTO(createdUser));
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
                                                      @RequestBody UserUpdateDTO request) {
        User updatedUser = userService.updateUser(
                id, request.getFullName(), request.getPhone(), request.getPosition(), request.isActive(),
                request.getStoreId(), request.getDepartmentId(), request.getRole(), request.getPassword()
        );
        return ResponseEntity.ok(toResponseDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /** Mapper to convert User -> UserResponseDTO */
    private UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setPosition(user.getPosition());
        dto.setDateOfJoining(user.getDateOfJoining());
        dto.setStoreId(user.getStore() != null ? user.getStore().getId() : null);
        dto.setDepartmentId(user.getDepartment() != null ? user.getDepartment().getId() : null);
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        return dto;
    }
}
