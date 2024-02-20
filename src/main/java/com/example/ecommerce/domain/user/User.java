package com.example.ecommerce.domain.user;

import com.example.ecommerce.domain.BaseEntity;
import com.example.ecommerce.global.enums.Authority;
import com.example.ecommerce.global.enums.Provider;
import com.example.ecommerce.global.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"provider", "provider_id"})
})
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "authority")
    private Authority authority;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "email", unique = true)
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "provider")
    private Provider provider; //google

    @Column(name = "provider_id")
    private String providerId; // google 계정 pk값

    public static User create(String username, String password, Authority authority, Role role, String email) {

        return User.builder()
                   .username(username)
                   .password(password)
                   .authority(authority)
                   .role(role)
                   .email(email)
                   .provider(Provider.NONE)
                   .providerId("")
                   .build();
    }

    public static User createAtGoogle(String username, String password, Authority authority, Role role, String email, String providerId) {

        return User.builder()
                   .username(username)
                   .password(password)
                   .authority(authority)
                   .role(role)
                   .email(email)
                   .provider(Provider.GOOGLE)
                   .providerId(providerId)
                   .build();
    }
}
