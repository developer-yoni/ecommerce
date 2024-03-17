package com.example.ecommerce.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Authority {

    USER("일반 회원"),
    MANAGER("중간 관리자"),
    AUTHORITY("관리자");

    private final String description;
}
