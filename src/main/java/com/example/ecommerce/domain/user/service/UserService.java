package com.example.ecommerce.domain.user.service;

import com.example.ecommerce.domain.user.User;
import com.example.ecommerce.domain.user.repository.UserRepository;
import com.example.ecommerce.global.response.ApiCode;
import com.example.ecommerce.global.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(String username, String password) {

        //1. 중복 검사 후
        if (userRepository.existsByUsername(username)) {

            throw new ApiException(ApiCode.CODE_000_0013, "이미 사용중인 username 입니다");
        }

        //2. 저장
        User user = User.createWithUserAuthority(username, passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
