package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Registration request details")
public class RegisterRequest {
    @NotBlank
    @Schema(description = "User's first name", example = "Aqueeb")
    private String firstName;
    @NotBlank
    @Schema(description = "User's last name", example = "Ahmed")
    private String lastName;
    @NotBlank
    @Email
    @Schema(description = "User's email address", example = "aqueeb@gmail.com")
    private String email;
    @Schema(description = "User's phone number", example = "9876543210")
    private String phone;
    @NotBlank
    @Size(min = 6)
    @Schema(description = "User's password", example = "password123")
    private String password;
}
