package com.example.ecommerce.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class SignInResponseDto {

    private String accessToken;

    public static SignInResponseDto toDto(String accessToken) {

        return SignInResponseDto.builder()
                                .accessToken(accessToken)
                                .build();
    }
}
