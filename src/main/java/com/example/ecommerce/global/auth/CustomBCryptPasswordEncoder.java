package com.example.ecommerce.global.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomBCryptPasswordEncoder {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String encode(String rawPassword) {

        return bCryptPasswordEncoder.encode(rawPassword);
    }
}
