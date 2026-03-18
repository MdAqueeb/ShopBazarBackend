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
@Schema(description = "Response containing dashboard metrics for a seller")
public class SellerDashboardResponse {
    @Schema(description = "Seller ID", example = "1")
    private Long sellerId;
    @Schema(description = "Store name", example = "Aqueeb's Store")
    private String storeName;
    @Schema(description = "Seller account status", example = "ACTIVE")
    private String status;
    @Schema(description = "Total number of products listed", example = "45")
    private Long totalProducts;
    @Schema(description = "Total number of orders received", example = "120")
    private Long totalOrders;
    @Schema(description = "Total revenue earned", example = "15000.75")
    private BigDecimal totalRevenue;
}
