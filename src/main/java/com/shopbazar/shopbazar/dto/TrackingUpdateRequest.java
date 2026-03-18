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
@Schema(description = "Request to add a new tracking update")
public class TrackingUpdateRequest {
    @Schema(description = "Tracking status", example = "IN_TRANSIT")
    private String status;
    @Schema(description = "Current location", example = "Mumbai, MH")
    private String location;
    @Schema(description = "Update description", example = "Package cleared customs")
    private String description;
}
