package com.shopbazar.shopbazar.dto;

import lombok.Data;

@Data
public class SellerApplyRequest {
    private Long userId;
    private String storeName;
    private String storeDescription;
    private String phone;
    private String gstNumber;
}
