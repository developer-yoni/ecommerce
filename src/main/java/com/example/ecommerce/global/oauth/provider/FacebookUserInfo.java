package com.example.ecommerce.global.oauth.provider;

import com.example.ecommerce.global.enums.Provider;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FacebookUserInfo implements OAuth2ServiceInfo{

    private final Map<String, Object> attributeMap;

    @Override
    public String getProviderId() {

        return (String) attributeMap.get("id");
    }

    @Override
    public Provider getProvider() {

        return Provider.FACEBOOK;
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
