package com.example.ecommerce.global.auth;

import com.example.ecommerce.domain.user.User;
import com.example.ecommerce.domain.user.enums.Authority;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class PrincipalDetails implements UserDetails {

    private final User user;
    private Long id;
    private String username;
    private Authority authority;

    public static PrincipalDetails convert(User user) {

        return PrincipalDetails.builder()
                               .user(user)
                               .build();
    }

    public static PrincipalDetails successAuthorize(Long id, String username, Authority authority) {

        return PrincipalDetails.builder()
                               .id(id)
                               .username(username)
                               .authority(authority)
                               .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Objects.nonNull(user) ? List.of(user.getAuthority()) : List.of(authority);
    }

    @Override
    public String getPassword() {

        return user.getPassword();
    }

    @Override
    public String getUsername() {

        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }
}
