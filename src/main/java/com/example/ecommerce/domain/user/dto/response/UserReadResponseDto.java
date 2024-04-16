package com.example.ecommerce.domain.user.dto.response;

import com.example.ecommerce.domain.user.User;
import com.example.ecommerce.global.enums.Authority;
import com.example.ecommerce.global.enums.Role;
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
    private String email;
    private Authority authority;
    private Role role;

    public static UserReadResponseDto toDto(User user) {

        return UserReadResponseDto.builder()
                                  .id(user.getId())
                                  .username(user.getUsername())
                                  .email(user.getEmail())
                                  .authority(user.getAuthority())
                                  .role(user.getRole())
                                  .build();
    }
}
