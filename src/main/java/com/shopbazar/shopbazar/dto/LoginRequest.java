package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@Schema(description = "Login request details")
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank
    @Email
    @Schema(description = "User's email address", example = "aqueeb@gmail.com")
    private String email;
    @NotBlank
    @Schema(description = "User's password", example = "password123")
    private String password;
}
