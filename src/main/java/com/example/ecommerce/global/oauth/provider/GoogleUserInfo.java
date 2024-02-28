package com.example.ecommerce.global.oauth.provider;

import com.example.ecommerce.global.enums.Provider;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoogleUserInfo implements OAuth2ServiceInfo{


    private final Map<String, Object> attributeMap;

    @Override
    public String getProviderId() {

        return (String) attributeMap.get("sub");
    }

    @Override
    public Provider getProvider() {

        return Provider.GOOGLE;
    }

    @Override
    public String getEmail() {

        return (String)attributeMap.get("email");
    }

    @Override
    public String getName() {

        return (String)attributeMap.get("name");
    }
}
