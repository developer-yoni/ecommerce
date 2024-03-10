package com.example.ecommerce.domain.user.controller;

import com.example.ecommerce.domain.user.dto.request.UserCreateRequestDto;
import com.example.ecommerce.domain.user.service.UserService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
}
