package com.example.ecommerce.global.oauth.provider;

import com.example.ecommerce.global.enums.Provider;


public interface OAuth2ServiceInfo {

    String getProviderId();

    Provider getProvider();

    String getEmail();

    String getName();
}
