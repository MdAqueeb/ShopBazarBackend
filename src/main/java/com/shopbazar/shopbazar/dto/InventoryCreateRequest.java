package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request to initialize inventory for a product")
public class InventoryCreateRequest {
    @Schema(description = "Product ID", example = "1")
    private Long productId;
    @Schema(description = "Initial stock level", example = "100")
    private Integer stock;
}
