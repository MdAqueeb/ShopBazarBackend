package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Reset password request details")
public class ResetPasswordRequest {
    @NotBlank
    @Size(min = 6)
    @Schema(description = "User's new password", example = "newPassword123")
    private String newPassword;
}
