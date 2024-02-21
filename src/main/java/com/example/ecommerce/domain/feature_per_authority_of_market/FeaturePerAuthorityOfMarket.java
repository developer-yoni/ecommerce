package com.example.ecommerce.domain.feature_per_authority_of_market;

import com.example.ecommerce.domain.authority.Authority;
import com.example.ecommerce.domain.feature.Feature;
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
@Table(name = "feature_per_authority_of_market")
public class FeaturePerAuthorityOfMarket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feature_per_authority_of_market_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "on_off")
    private OnOff onOff;

    /**
     * [Mapping 관계]
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_key")
    private Market market;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_key")
    private Authority authority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_key")
    private Feature feature;

    /**
     * [생성 메서드]
     * */
    public static FeaturePerAuthorityOfMarket create(OnOff onOff, Market market, Authority authority, Feature feature) {

        return FeaturePerAuthorityOfMarket.builder()
                                          .onOff(onOff)
                                          .market(market)
                                          .authority(authority)
                                          .feature(feature)
                                          .build();
    }
}
