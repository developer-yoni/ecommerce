package com.example.ecommerce.global.auth;

import com.example.ecommerce.domain.user.User;
import com.example.ecommerce.global.enums.EntityStatus;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * [존재 이유]
 *  - Security가 /login을 낚아채서 대신 로그인을 진행시킨다.
 *  - 그리고 로그인이 완료가 되면, security session을 만들어 준다
 *  - 그런데 그 security session 안에 -> authentication 객체가 들어갈 수 있고 -> 그 authentication 객체 안에 들어갈 수 있는 user 정보는 userDetails 타입이어야 함
 *
 *  - 결론적으로 로그인에 성공했을 때 -> security session안에 넣을 userDetails 정보를 -> 우리가 우리 나름대로 커스텀 하면 좋고 -> 그 대상이 바로 PrincipalDetails (정의하기나름이겠지)
 * */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class PrincipalDetails implements UserDetails {


    private User user;

    /**
     * [생성 factory method]
     * */
    public static PrincipalDetails convert(@NonNull  User user) {

        return PrincipalDetails.builder()
                               .user(user)
                               .build();
    }

    // 해당 User의 권한을 리턴하는 책임
    // 람다식이 사실 -> 구현해야 할 메소드가 하나인 경우의 인터페이스의 유일한 메소드를 implement 하면서, 익명클래스 형태로 그 인터페이스를 구현하는 구현체 생성으로 이어짐
    // 즉 내가 적은 () -> user.getAuthrotiy().getValue() 는 -> GrantedAuthority 인터페이스의 구현체 생성으로 이어지고 -> 이를 List로 감싸서 리턴하면 끝!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(() -> user.getAuthority().getValue());
    }

    @Override
    public String getPassword() {

        return user.getPassword();
    }

    @Override
    public String getUsername() {

        return user.getUsername();
    }

    /**
     * - 지금은 일단 ACTIVE / INACTIVE 상태로만 구분하지만 (즉 사실상 Enable만 구분)
     * - user data의 경우 , 아래의 세부 사항들 (휴먼 회원 , 비번 몇회 이상 실패 or 관리자 잠금, 비번 주기적 변경 , 회원 탁퇴 여부)을 체크하는 용도로 사용된다
     * - 단 이를 실제 서비스에서 어떻게 사용할지는 좀더 세부적으로 고민해야 하고, 나는 일단 ACTIVE 여부로만 쉽게 판단함 
     * */

    // 만료되지 않았는지 여부 -> ex 휴먼 회원
    @Override
    public boolean isAccountNonExpired() {

        return user.getEntityStatus().equals(EntityStatus.ACTIVE);
    }

    // 계정이 잠겨있지 않은지 여부 -> ex 비밀번호 몇회 이상 실패 or 관리자 잠금
    @Override
    public boolean isAccountNonLocked() {

        return user.getEntityStatus().equals(EntityStatus.ACTIVE);
    }

    // 사용자의 인증 정보(주로 비밀번호)가 만료되지 않았는지를 나타냅니다. -> 예를 들어, 비밀번호를 정기적으로 변경하도록 강제하는 정책이 있을 때 사용됩니다.
    @Override
    public boolean isCredentialsNonExpired() {

        return user.getEntityStatus().equals(EntityStatus.ACTIVE);
    }

    // 사용자 계정이 활성화(사용 가능) 상태인지를 나타냅니다. -> ex 회원 탈퇴 , 이메일 인증 완료 여부나 관리자에 의한 계정 활성화 상태 등을 체크
    @Override
    public boolean isEnabled() {

        return user.getEntityStatus().equals(EntityStatus.ACTIVE);
    }
}
