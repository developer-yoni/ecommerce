package com.example.ecommerce.domain.user.dto.response;

import com.example.ecommerce.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class UserCreateResponseDto {

    private Long userId;

    public static UserCreateResponseDto toDto(User user) {

        return UserCreateResponseDto.builder()
                                    .userId(user.getId())
                                    .build();
    }
}
