package com.shopbazar.shopbazar.dto;

import lombok.Data;

@Data
public class CategoryCreateRequest {
    private String name;
    private Long parentId; // Optional, null means it's a root category
}
