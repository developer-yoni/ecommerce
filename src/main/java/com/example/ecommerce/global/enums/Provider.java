package com.example.ecommerce.global.enums;

import java.util.Arrays;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {

    NONE("none", "자체 회원가입"){
        @Override
        public String getProviderId(Map<String, Object> attributeMap) {

            return "none";
        }

        @Override
        public String getName(Map<String, Object> attributeMap) {

            return "none";
        }

        @Override
        public String getEmail(Map<String, Object> attributeMap) {

            return "none";
        }
    },
    GOOGLE("google", "GOOGLE"){
        @Override
        public String getProviderId(Map<String, Object> attributeMap) {

            return (String) attributeMap.get("sub");
        }
        @Override
        public String getName(Map<String, Object> attributeMap) {

            return (String) attributeMap.get("name");
        }

        @Override
        public String getEmail(Map<String, Object> attributeMap) {

            return (String) attributeMap.get("email");
        }
    },
    FACEBOOK("facebook", "FACEBOOK"){
        @Override
        public String getProviderId(Map<String, Object> attributeMap) {

            return (String) attributeMap.get("id");
        }
        @Override
        public String getName(Map<String, Object> attributeMap) {

            return (String) attributeMap.get("name");
        }

        @Override
        public String getEmail(Map<String, Object> attributeMap) {

            return (String) attributeMap.get("email");
        }
    },
    NAVER("naver", "NAVER"){
        @Override
        public String getProviderId(Map<String, Object> attributeMap) {

            Map<String, Object> response = (Map<String, Object>) attributeMap.get("response");
            return (String) response.get("id");
        }

        @Override
        public String getName(Map<String, Object> attributeMap) {

            Map<String, Object> response = (Map<String, Object>) attributeMap.get("response");
            return (String) response.get("name");
        }

        @Override
        public String getEmail(Map<String, Object> attributeMap) {

            Map<String, Object> response = (Map<String, Object>) attributeMap.get("response");
            return (String) response.get("email");
        }
    };

    private final String value;
    private final String description;

    // 추상메소드
    public abstract String getProviderId(Map<String, Object> attributeMap);
    public abstract String getName(Map<String, Object> attributeMap);
    public abstract String getEmail(Map<String, Object> attributeMap);

    public static Provider convert(String value) {

        return Arrays.stream(Provider.values())
                     .filter(provider -> provider.value.equals(value))
                     .findFirst()
                     .orElse(NONE);
    }
}
