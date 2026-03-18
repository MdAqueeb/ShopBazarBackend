package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Authentication response containing JWT tokens")
public class AuthResponse {
    @Schema(description = "Access JWT token", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String accessToken;
    @Schema(description = "Refresh token", example = "550e8400-e29b-41d4-a716-446655440000")
    private String refreshToken;
    @Schema(description = "User ID", example = "1")
    private Long userId;
    @Schema(description = "User's email", example = "aqueeb@gmail.com")
    private String email;
    @Schema(description = "User's role", example = "CUSTOMER")
    private String role;
}
