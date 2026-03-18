package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Refresh token request details")
public class RefreshTokenRequest {
    @NotBlank
    @Schema(description = "Refresh token string", example = "550e8400-e29b-41d4-a716-446655440000")
    private String refreshToken;
}
