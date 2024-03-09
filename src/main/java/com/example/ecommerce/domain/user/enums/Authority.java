package com.example.ecommerce.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum Authority implements GrantedAuthority {

    USER("user", "일반사용자 권한"),
    MANAGER("manager", "관리자 권한"),
    ADMIN("admin", "회사 계정 권한");


    private final String value;
    private final String description;

    @Override
    public String getAuthority() {

        return value;
    }
}
