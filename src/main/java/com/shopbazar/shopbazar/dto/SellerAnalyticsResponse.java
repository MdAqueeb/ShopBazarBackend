package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing sales analytics for a seller")
public class SellerAnalyticsResponse {
    @Schema(description = "Seller ID", example = "1")
    private Long sellerId;
    @Schema(description = "Total revenue earned", example = "15000.75")
    private BigDecimal totalRevenue;
    @Schema(description = "Total number of orders received", example = "120")
    private Long totalOrders;
}
