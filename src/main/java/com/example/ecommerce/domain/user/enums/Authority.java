package com.example.ecommerce.domain.user.enums;

import com.example.ecommerce.global.response.ApiCode;
import com.example.ecommerce.global.response.ApiException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum Authority implements GrantedAuthority {

    USER("USER", "일반사용자 권한"),
    MANAGER("MANAGER", "관리자 권한"),
    ADMIN("ADMIN", "회사 계정 권한");


    private final String value;
    private final String description;

    @Override
    public String getAuthority() {

        return value;
    }

    public static Authority convert(String value) {

        return Arrays.stream(Authority.values())
                     .filter(authority -> authority.getValue().equals(value))
                     .findFirst()
                     .orElseThrow(() -> {
                         throw new ApiException(ApiCode.CODE_000_0014, "Authority value값을 기반으로 Enum으로 변환하는 과정에서 예외");
                     });
    }
}
