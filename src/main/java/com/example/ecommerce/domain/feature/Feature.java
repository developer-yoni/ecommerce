package com.example.ecommerce.domain.feature;

import com.example.ecommerce.domain.authority.Authority;
import com.example.ecommerce.domain.authority.AuthorityType;
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
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "feature_type")
    private FeatureType featureType;

    /**
     * [Mapping 관계]
     * */

    /**
     * [생성 메서드]
     * */
    public static Feature create(FeatureType featureType) {

        return Feature.builder()
                      .featureType(featureType)
                      .build();
    }
}
