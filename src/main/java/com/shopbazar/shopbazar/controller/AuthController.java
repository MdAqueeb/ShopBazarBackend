package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.dto.*;
import com.shopbazar.shopbazar.entity.User;
import com.shopbazar.shopbazar.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration, login, and password management")
public class AuthController {

        private final AuthService authService;

        @Operation(summary = "Register a new user", description = "Creates a new user account and sends a verification email")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "User registered successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid input data"),
                        @ApiResponse(responseCode = "409", description = "User with this email already exists")
        })
        @PostMapping("/register")
        public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<User>> register(
                        @Valid @RequestBody RegisterRequest request) {
                User usr = authService.register(request);
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(com.shopbazar.shopbazar.dto.ApiResponse.<User>builder()
                                                .success(true)
                                                .message("User registered successfully. Please verify your email.")
                                                .data(usr)
                                                .build());
        }

        @Operation(summary = "Login user", description = "Authenticates a user and returns JWT tokens")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Login successful"),
                        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
                        @ApiResponse(responseCode = "403", description = "Account is locked or disabled")
        })
        @PostMapping("/login")
        public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<AuthResponse>> login(
                        @Valid @RequestBody LoginRequest request) {
                AuthResponse response = authService.login(request);
                return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<AuthResponse>builder()
                                .success(true)
                                .message("Login successful")
                                .data(response)
                                .build());
        }

        @Operation(summary = "Refresh JWT token", description = "Uses a valid refresh token to generate a new access token")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
                        @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
        })
        @PostMapping("/refresh-token")
        public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<AuthResponse>> refreshToken(
                        @Valid @RequestBody RefreshTokenRequest request) {
                AuthResponse response = authService.refreshToken(request);
                return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<AuthResponse>builder()
                                .success(true)
                                .message("Token refreshed successfully")
                                .data(response)
                                .build());
        }

        @Operation(summary = "Logout user", description = "Invalidates the user's refresh token")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Logged out successfully")
        })
        @PostMapping("/logout")
        public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Void>> logout(
                        @RequestBody RefreshTokenRequest request) {
                authService.logout(request.getRefreshToken());
                return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Void>builder()
                                .success(true)
                                .message("Logged out successfully")
                                .build());
        }

        @Operation(summary = "Forgot password", description = "Sends a password reset link to the user's email")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reset link sent"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        @PostMapping("/forgot-password")
        public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Void>> forgotPassword(
                        @Valid @RequestBody ForgotPasswordRequest request) {
                authService.forgotPassword(request);
                return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Void>builder()
                                .success(true)
                                .message("Password reset link sent to your email")
                                .build());
        }

        @Operation(summary = "Reset password", description = "Resets the user's password using a valid reset token")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Password reset successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid or expired reset token")
        })
        @PostMapping("/reset-password")
        public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Void>> resetPassword(
                        @Parameter(description = "Password reset token sent via email", required = true) @RequestParam String token,
                        @Valid @RequestBody ResetPasswordRequest request) {
                authService.resetPassword(token, request);
                return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Void>builder()
                                .success(true)
                                .message("Password reset successfully")
                                .build());
        }

        @Operation(summary = "Verify email", description = "Verifies the user's email address using a valid verification token")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Email verified successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid or expired verification token")
        })
        @PostMapping("/verify-email")
        public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Void>> verifyEmail(
                        @Parameter(description = "Verification token sent via email", required = true) @RequestParam String token) {
                authService.verifyEmail(token);
                return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Void>builder()
                                .success(true)
                                .message("Email verified successfully")
                                .build());
        }
}
