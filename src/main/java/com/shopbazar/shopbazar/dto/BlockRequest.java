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
@Schema(description = "Request to block a user or seller")
public class BlockRequest {
    @Schema(description = "Reason for blocking", example = "Violation of terms and conditions")
    private String reason;
}
