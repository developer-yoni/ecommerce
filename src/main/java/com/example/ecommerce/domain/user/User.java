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

    // 일반 회원가입 User와 , OAuth2.0 회원가입 User를 별도의 Entity Row로 쌓는다면 -> 필연적으로 이 email unique contraint 에서 걸림 -> 즉 별도의 entity row로 쌓는건 적절한 방식이 아니라고 예상
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "name")
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "provider")
    private Provider provider; // GOOGLE, NAVER

    @Column(name = "provider_id")
    private String providerId; // 각 Provider 서버 에서 해당 회원의 PK 값

    public static User create(String username, String password, Authority authority, Role role, String email) {

        return User.builder()
                   .username(username)
                   .password(password)
                   .authority(authority)
                   .role(role)
                   .email(email)
                   .provider(Provider.NONE)
                   .providerId(null) // null이 좀 그렇다면, unique 제약조건을 이용해서 email로 넣던가 or save() 이후 id값으로 넣던가
                   .build();
    }

    public static User createAtOAuth2(String username, String password, Authority authority, Role role, String email, String name, Provider provider, String providerId) {

        return User.builder()
                   .username(username)
                   .password(password)
                   .authority(authority)
                   .role(role)
                   .email(email)
                   .name(name)
                   .provider(provider)
                   .providerId(providerId)
                   .build();
    }

    /**
     * [변경 메서드]
     * */
    public void changeProviderIdAfterSave(Long id) {

        if (this.provider.equals(Provider.NONE)) {

            this.providerId = String.valueOf(id);
        }
    }

    public void changeEmail(String email) {

        this.email = email;
    }
}
