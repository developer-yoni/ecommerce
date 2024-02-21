package com.example.ecommerce.domain.feature;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeatureType {

    PRODUCT_CREATE("상품 등록"),
    PRODUCT_UPDATE("상품 수정"),
    PRODUCT_DELETE("상품 삭제");

    private final String description;
}
