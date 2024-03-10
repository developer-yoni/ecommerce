package com.example.ecommerce.domain.user.controller;

import com.example.ecommerce.domain.user.dto.request.UserCreateRequestDto;
import com.example.ecommerce.domain.user.dto.response.UserReadResponseDto;
import com.example.ecommerce.domain.user.enums.Authority;
import com.example.ecommerce.domain.user.service.UserService;
import com.example.ecommerce.global.auth.PrincipalDetails;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity signUp(@Valid @ModelAttribute UserCreateRequestDto userCreateRequestDto) {

        userService.createUser(userCreateRequestDto.getUsername(), userCreateRequestDto.getPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/v1/user")
    public ResponseEntity getUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        return ResponseEntity.ok(UserReadResponseDto.toDto(principalDetails.getUser().getId(),
                                                           principalDetails.getUser().getUsername(),
                                                           principalDetails.getUser().getAuthority()));
    }
}
