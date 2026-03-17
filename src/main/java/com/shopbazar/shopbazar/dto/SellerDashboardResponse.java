package com.shopbazar.shopbazar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerDashboardResponse {
    private Long sellerId;
    private String storeName;
    private String status;
    private Long totalProducts;
    private Long totalOrders;
    private BigDecimal totalRevenue;
}
