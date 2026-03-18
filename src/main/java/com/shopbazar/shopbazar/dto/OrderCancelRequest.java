package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to cancel an order")
public class OrderCancelRequest {
    @Schema(description = "Reason for cancellation", example = "Changed my mind")
    private String reason;
}
