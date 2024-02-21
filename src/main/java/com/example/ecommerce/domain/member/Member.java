package com.example.ecommerce.domain.member;

import com.example.ecommerce.domain.authority.Authority;
import com.example.ecommerce.domain.authority.AuthorityType;
import com.example.ecommerce.domain.market.Market;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "member")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "name")
    private String name;

    /**
     * [역정규화 필드]
     * */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "authority_type")
    private AuthorityType authorityType;

    /**
     * [Mapping 관계]
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_key")
    private Market market;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_key")
    private Authority authority;

    /**
     * [생성 메서드]
     * */
    public static Member create(String name, Market market, Authority authority) {

        return Member.builder()
                     .name(name)
                     .authorityType(authority.getAuthorityType())
                     .market(market)
                     .authority(authority)
                     .build();
    }
}
