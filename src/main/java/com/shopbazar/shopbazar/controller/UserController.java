package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.entity.User;
import com.shopbazar.shopbazar.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// import com.shopbazar.shopbazar.dto.ApiResponse;
// import com.shopbazar.shopbazar.dto.UserUpdateRequest;
// import com.shopbazar.shopbazar.dto.UserStatusRequest;
// import com.shopbazar.shopbazar.dto.UserRoleRequest;
// import com.shopbazar.shopbazar.dto.UserDeleteRequest;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for managing user profiles, status, and system-wide user administration")
public class UserController {

    private final UserService userService;

    // 1. Get user profile
    @Operation(summary = "Get user profile", description = "Retrieves a user's basic profile information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<User>> getUserProfile(
            @Parameter(description = "ID of the user", required = true) @PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<User>builder()
                .success(true)
                .message("User profile retrieved successfully")
                .data(user)
                .build());
    }

    // 2. Update user profile
    @Operation(summary = "Update user profile", description = "Updates a user's profile information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<User>> updateUserProfile(
            @Parameter(description = "ID of the user to update", required = true) @PathVariable Long userId,
            @RequestBody User userDetails) {
        User updatedUser = userService.updateUserProfile(userId, userDetails);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<User>builder()
                .success(true)
                .message("User profile updated successfully")
                .data(updatedUser)
                .build());
    }

    // 3. Delete user profile
    @Operation(summary = "Delete user", description = "Permanently deletes a user's account and information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Void>> deleteUserProfile(
            @Parameter(description = "ID of the user to delete", required = true) @PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Void>builder()
                .success(true)
                .message("User profile deleted successfully")
                .data(null)
                .build());
    }

    // 4. Get all users for admin with pagination
    @Operation(summary = "Get all users", description = "Retrieves a paginated list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Page<User>>> getAllUsers(Pageable pageable) {
        Page<User> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Page<User>>builder()
                .success(true)
                .message("Users retrieved successfully")
                .data(users)
                .build());
    }

    // 5. Update user status
    @Operation(summary = "Update user status", description = "Sets the user account status (e.g., Active, Inactive, Blocked)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{userId}/status")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<User>> updateUserStatus(
            @Parameter(description = "ID of the user", required = true) @PathVariable Long userId,
            @Parameter(description = "New status for the user account", required = true) @RequestParam User.Status status) {
        User updatedUser = userService.updateUserStatus(userId, status);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<User>builder()
                .success(true)
                .message("User status updated successfully")
                .data(updatedUser)
                .build());
    }

    // 6. Update user role
    @Operation(summary = "Update user role", description = "Assigns a new role or privilege level to a user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{userId}/role")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<User>> updateUserRole(
            @Parameter(description = "ID of the user", required = true) @PathVariable Long userId,
            @Parameter(description = "ID of the role to assign", required = true) @RequestParam Long roleId) {
        User updatedUser = userService.updateUserRole(userId, roleId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<User>builder()
                .success(true)
                .message("User role updated successfully")
                .data(updatedUser)
                .build());
    }
}
