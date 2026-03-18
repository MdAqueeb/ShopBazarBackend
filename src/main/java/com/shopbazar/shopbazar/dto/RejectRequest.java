package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request to reject a seller application")
public class RejectRequest {
    @Schema(description = "Reason for rejection", example = "Invalid GST number or missing documentation")
    private String reason;
}
