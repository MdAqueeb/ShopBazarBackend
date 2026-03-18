package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response containing platform-wide analytics metrics")
public class PlatformAnalyticsResponse {
    @Schema(description = "Total number of registered users", example = "1200")
    private Long totalUsers;
    @Schema(description = "Total number of active sellers", example = "50")
    private Long totalSellers;
    @Schema(description = "Total number of products listed", example = "5000")
    private Long totalProducts;
    @Schema(description = "Total number of orders processed", example = "2500")
    private Long totalOrders;
    @Schema(description = "Total platform revenue generated", example = "250000.50")
    private BigDecimal totalRevenue;
}
