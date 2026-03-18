package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Forgot password request details")
public class ForgotPasswordRequest {
    @NotBlank
    @Email
    @Schema(description = "User's registered email address", example = "aqueeb@gmail.com")
    private String email;
}
