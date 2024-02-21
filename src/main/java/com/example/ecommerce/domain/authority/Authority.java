package com.example.ecommerce.domain.authority;

import com.example.ecommerce.domain.market.Market;
import com.example.ecommerce.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "authority")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "authority_type")
    private AuthorityType authorityType;

    /**
     * [Mapping 관계]
     * */

    /**
     * [생성 메서드]
     * */
    public static Authority create(AuthorityType authorityType) {

        return Authority.builder()
                        .authorityType(authorityType)
                        .build();
    }
}
