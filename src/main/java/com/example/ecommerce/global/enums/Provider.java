package com.example.ecommerce.global.enums;

import com.example.ecommerce.global.response.ApiCode;
import com.example.ecommerce.global.response.ApiException;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {

    NONE("none", "자체 회원가입"),
    GOOGLE("google", "GOOGLE");

    private final String value;
    private final String description;

    public static Provider getByValue(String value) {

        return Arrays.stream(Provider.values())
                     .filter(provider -> provider.value.equals(value))
                     .findFirst()
                     .orElse(NONE);
    }
}
