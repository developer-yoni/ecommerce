package com.example.ecommerce.global.auth;

import com.example.ecommerce.domain.user.User;
import com.example.ecommerce.domain.user.repository.UserRepository;
import com.example.ecommerce.global.enums.EntityStatus;
import com.example.ecommerce.global.response.ApiCode;
import com.example.ecommerce.global.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * [언제 UserDetailsService 구현체가 동작하냐]
 * - 우리는 SecurityConfig에서 loginProcessingUrl("/login")을 설정함
 * - 이 설정은 /login 요청이 왔을 때 -> 비로소 Security가 자동으로 로그인 처리를 해주는 설정!
 *
 * - 그렇다면 Security는 어떻게 로그인 처리를 해주냐?
 * - 바로 이 , UserDetailsService 구현체를 사용해서 UserDetails를 로딩 + 이후 로딩된 UserDetails를 가지고 AuthenticationManager가 인증 처리! (Security가 그렇게 구현되어 있음)
 * - 이 UserDetailsService의 규약인 loadUserByUsername()을 호출하여 -> 로딩한 UserDetails를 가지고 AuthenticationManager가 로그인 성공 여부를 판단한다!
 * - 단 UserDetailsService는 인터페이스 이기 때문에, 이를 어떻게 구현하든 자유이고 , 우리는 DB에 저장된 User 정보를 근거로 사용하도록 구현한다!
 *
 * - 즉 UserDetailService 구현체는 -> [사용자 정보인 UserDetails를 로딩]하는 부분만을 담당
 * - 이후 [사용자 인증(비밀번호 검증 포함)은] -> 스프링 시큐리티의 AuthenticationManager가 처리한다!
 * */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * [여기서 리턴되는 UserDetails 구현체는]
     *  - Authenticaiton 객체 안에 들어가고
     *  - 그 Authenticaiton 객체는 SecurityContextHolder 안에 들어가서
     *  - 그 SecurityContextHodler가 Session에 저장된다
     *  - (인증에 성공했을 경우만)
     *  - (인증에 실패하면, 로딩한 UserDetails는 인증된 Authentication안에 포함되어 SecurityContextHolder안에 포함되지 않는다)
     * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameAndEntityStatus(username, EntityStatus.ACTIVE)
                                  .orElseThrow(() -> new ApiException(ApiCode.CODE_000_0011,
                                                                      "PrincipalDetailsService.loadUserByUsername() 에서 User 객체 조회 실패"));

        return PrincipalDetails.convert(user);
    }
}
