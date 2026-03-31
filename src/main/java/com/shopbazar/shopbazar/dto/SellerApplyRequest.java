package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor 
@Schema(description = "Request to apply for a seller account")
public class SellerApplyRequest {
    @Schema(description = "User ID applying for seller status", example = "1")
    private Long userId;
    @Schema(description = "Desired store name", example = "Aqueeb's Electronics")
    private String storeName;
    @Schema(description = "Store description", example = "Direct seller of premium electronics")
    private String storeDescription;
    @Schema(description = "Contact phone number", example = "9876543210")
    private String phone;
    @Schema(description = "GST identification number", example = "22AAAAA0000A1Z5")
    private String gstNumber;
}
