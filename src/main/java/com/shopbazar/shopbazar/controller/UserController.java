package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.dto.ApiResponse;
import com.shopbazar.shopbazar.entity.User;
import com.shopbazar.shopbazar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1. Get user profile
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> getUserProfile(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.<User>builder()
                .success(true)
                .message("User profile retrieved successfully")
                .data(user)
                .build());
    }

    // 2. Update user profile
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> updateUserProfile(
            @PathVariable Long userId, @RequestBody User userDetails) {
        User updatedUser = userService.updateUserProfile(userId, userDetails);
        return ResponseEntity.ok(ApiResponse.<User>builder()
                .success(true)
                .message("User profile updated successfully")
                .data(updatedUser)
                .build());
    }

    // 3. Delete user profile
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUserProfile(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("User profile deleted successfully")
                .data(null)
                .build());
    }

    // 4. Get all users for admin with pagination
    @GetMapping
    public ResponseEntity<ApiResponse<Page<User>>> getAllUsers(Pageable pageable) {
        Page<User> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.<Page<User>>builder()
                .success(true)
                .message("Users retrieved successfully")
                .data(users)
                .build());
    }

    // 5. Update user status
    @PatchMapping("/{userId}/status")
    public ResponseEntity<ApiResponse<User>> updateUserStatus(
            @PathVariable Long userId, @RequestParam User.Status status) {
        User updatedUser = userService.updateUserStatus(userId, status);
        return ResponseEntity.ok(ApiResponse.<User>builder()
                .success(true)
                .message("User status updated successfully")
                .data(updatedUser)
                .build());
    }

    // 6. Update user role
    @PutMapping("/{userId}/role")
    public ResponseEntity<ApiResponse<User>> updateUserRole(
            @PathVariable Long userId, @RequestParam Long roleId) {
        User updatedUser = userService.updateUserRole(userId, roleId);
        return ResponseEntity.ok(ApiResponse.<User>builder()
                .success(true)
                .message("User role updated successfully")
                .data(updatedUser)
                .build());
    }
}
