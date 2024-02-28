package com.example.ecommerce.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum Authority implements GrantedAuthority {

    USER("USER","일반 회원"),
    MANAGER("MANAGER","중간 관리자"),
    ADMIN("ADMIN", "관리자");

    private final String value;
    private final String description;

    @Override
    public String getAuthority() {

        return value;
    }
}
