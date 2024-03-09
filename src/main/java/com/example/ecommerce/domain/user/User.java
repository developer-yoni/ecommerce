package com.example.ecommerce.domain.user;

import com.example.ecommerce.domain.BaseEntity;
import com.example.ecommerce.domain.user.enums.Authority;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "authority")
    private Authority authority;

    /**
     * [Foreign Key]
      */

    /**
     * [Create Static Factory Method]
     */
    public static User createWithUserAuthority(String username, String password) {

        return User.builder()
                   .username(username)
                   .password(password)
                   .authority(Authority.USER)
                   .build();
    }

}
