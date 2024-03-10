package com.example.ecommerce.domain.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateRequestDto {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
