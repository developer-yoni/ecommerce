package com.example.ecommerce.domain.authority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthorityType {

    PRIMARY("1순위 권한"),
    OWNER("2순위 권한"),
    TRAINER("3순위 권한");

    private final String description;
}
