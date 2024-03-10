package com.example.ecommerce.global.auth;

import com.example.ecommerce.domain.user.User;
import com.example.ecommerce.domain.user.repository.UserRepository;
import com.example.ecommerce.global.response.ApiCode;
import com.example.ecommerce.global.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrincipalUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * [언제 동작하는가]
     * - POST, /login (혹은 별도로 등록한 로그인 요청 url이 올떄)
     * - UsernamePasswordAuthenticationFilter가 가지고 있는 AuthenticationManager의 authenticate() 메소드내부에서 인증을 검증할 때
     * - 동작한다
     * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("***** 2. 로그인 시도 - DaoAuthenticationProvider에 의해 PrincipalDetailsService에 진입 *****");

        //1. username으로 조회하여 리턴
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            throw new UsernameNotFoundException("username 또는 password가 잘못되었습니다");
        });

        //2. 응답
        return PrincipalDetails.convert(user);
    }
}
