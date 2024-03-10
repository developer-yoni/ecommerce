package com.example.ecommerce.domain.user.dto.response;

import com.example.ecommerce.domain.user.enums.Authority;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class UserReadResponseDto {

    private Long id;
    private String username;
    private Authority authority;

    public static UserReadResponseDto toDto(Long id, String username, Authority authority) {

        return UserReadResponseDto.builder()
                                  .id(id)
                                  .username(username)
                                  .authority(authority)
                                  .build();
    }
}
