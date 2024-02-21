package com.example.ecommerce.domain.feature_per_authority_of_market;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OnOff {

    ON("on"),
    OFF("off");

    private final String description;
}
