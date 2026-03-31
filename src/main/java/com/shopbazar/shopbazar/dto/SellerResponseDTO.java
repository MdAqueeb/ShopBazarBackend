package com.shopbazar.shopbazar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerResponseDTO {
    private Long sellerId;
    private String storeName;
    private String status;
}
