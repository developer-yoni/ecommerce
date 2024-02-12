package com.example.ecommerce.domain.user.service;

import com.example.ecommerce.domain.user.User;
import com.example.ecommerce.domain.user.dto.response.UserCreateResponseDto;
import com.example.ecommerce.domain.user.dto.response.UserNicknameResponseDto;
import com.example.ecommerce.domain.user.repository.UserRepository;
import com.example.ecommerce.global.enums.Authority;
import com.example.ecommerce.global.enums.EntityStatus;
import com.example.ecommerce.global.enums.Role;
import com.example.ecommerce.global.response.ApiCode;
import com.example.ecommerce.global.response.ApiException;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void signUp(String username, String password, String email, String nickname) {

        //1. 동일 username으로 된 User가 존재하는지 확인 후  (회원 탈퇴한 User의 username도 쓰면 안됨 주의)
        if (userRepository.existsByUsername(username)) {

            throw new ApiException(ApiCode.CODE_000_0013, ApiCode.CODE_000_0013.getDescription());
        }

        //2. 생성 및 저장
        User user = User.create(username, passwordEncoder.encode(password), Authority.USER, Role.ROLE_USER, email, nickname);
        userRepository.save(user);
    }

    @Transactional
    public UserNicknameResponseDto editNickname(Long userId) {

        //1. memberId로 Member 조회
        User user = userRepository.findByIdAndEntityStatus(userId, EntityStatus.ACTIVE).orElseThrow(() -> {

            throw new ApiException(ApiCode.CODE_000_0011, "user 조회 실패");
        });


        //2. nickname 변경
        // 단 memberId가 10의 배수면 예외 발생
//        if (userId % 10L == 0) {
//
//            throw new ApiException(ApiCode.CODE_000_0014, "임시 예외 발생 " + userId);
//        }
        String originalNickname = user.getNickname();
        user.changeNickname(originalNickname + " 변경");

        return UserNicknameResponseDto.toDto(user);
    }

    @Transactional
    public UserNicknameResponseDto editNicknameOnError(Long userId) {

        //1. memberId로 Member 조회
        User user = userRepository.findByIdAndEntityStatus(userId, EntityStatus.ACTIVE).orElseThrow(() -> {

            throw new ApiException(ApiCode.CODE_000_0011, "user 조회 실패");
        });


        //2. nickname 변경
        //단 memberId가 10의 배수면 예외 발생
        if (userId % 2L == 0) {

            throw new ApiException(ApiCode.CODE_000_0014, "임시 예외 발생 " + userId);
        }

        String originalNickname = user.getNickname();
        user.changeNickname(originalNickname + " 변경");

        return UserNicknameResponseDto.toDto(user);
    }
}
