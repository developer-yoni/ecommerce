package com.example.ecommerce.global.oauth;

import com.example.ecommerce.global.enums.Authority;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class CustomOauth2User implements OAuth2User {

    private OAuth2User oAuth2User;
    private Authority authority;

    @Override
    public Map<String, Object> getAttributes() {

        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(authority);
    }

    @Override
    public String getName() {

        return oAuth2User.getAttribute("name");
    }

    public static CustomOauth2User convert(OAuth2User oAuth2User, Authority authority) {

        return CustomOauth2User.builder()
                               .oAuth2User(oAuth2User)
                               .authority(authority)
                               .build();
    }
}
