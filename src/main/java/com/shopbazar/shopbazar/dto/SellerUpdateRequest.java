package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request to update seller profile")
public class SellerUpdateRequest {
    @Schema(description = "New store name", example = "Aqueeb's Gadgets")
    private String storeName;
    @Schema(description = "New store description", example = "Updated description for the store")
    private String description;
}
