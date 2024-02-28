package com.example.ecommerce.domain.user.service;

import com.example.ecommerce.domain.user.User;
import com.example.ecommerce.domain.user.dto.response.UserCreateResponseDto;
import com.example.ecommerce.domain.user.repository.UserRepository;
import com.example.ecommerce.global.enums.Authority;
import com.example.ecommerce.global.enums.Role;
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
    public void signUp(String username, String password, String email) {

        //1. 동일 username으로 된 User가 존재하는지 확인 후  (회원 탈퇴한 User의 username도 쓰면 안됨 주의)
        if (userRepository.existsByUsername(username)) {

            throw new ApiException(ApiCode.CODE_000_0013, ApiCode.CODE_000_0013.getDescription());
        }

        //2. 생성 및 저장
        User user = User.create(username, passwordEncoder.encode(password), Authority.USER, Role.ROLE_USER, email);
        userRepository.save(user);
    }
}
