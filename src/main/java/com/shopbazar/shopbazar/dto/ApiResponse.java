package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Generic wrapper for all API responses")
public class ApiResponse<T> {
    @Schema(description = "Indicates if the operation was successful", example = "true")
    private boolean success;
    @Schema(description = "Descriptive message about the operation outcome", example = "Operation completed successfully")
    private String message;
    @Schema(description = "The payload data of the response")
    private T data;
}
