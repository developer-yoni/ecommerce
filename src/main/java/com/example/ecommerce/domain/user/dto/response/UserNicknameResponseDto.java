package com.example.ecommerce.domain.user.dto.response;

import com.example.ecommerce.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class UserNicknameResponseDto {

    private String nickname;

    public static  UserNicknameResponseDto toDto(User user) {

        return UserNicknameResponseDto.builder()
                                      .nickname(user.getNickname())
                                      .build();
    }
}
