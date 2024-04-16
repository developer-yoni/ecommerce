package com.example.ecommerce.global.oauth;

import com.example.ecommerce.domain.user.User;
import com.example.ecommerce.domain.user.repository.UserRepository;
import com.example.ecommerce.global.auth.PrincipalDetails;
import com.example.ecommerce.global.enums.Authority;
import com.example.ecommerce.global.enums.EntityStatus;
import com.example.ecommerce.global.enums.Provider;
import com.example.ecommerce.global.enums.Role;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {


    private final UserRepository userRepository;
    //private final CustomBCryptPasswordEncoder passwordEncoder;

    /**
     * [구글 로그인에 성공했을 때, 후처리 되는 함수]
     * */
    // OAuth2uSERequest에는 구글 로그인에 성공시 넘겨받은 -> accessToken + (그 accessToken)으로 구글 리소스 서버에 접근 가능한 사용자 정보 -> 가 포함되어 있음

    // 1. 구글 로그인 버튼 클릭
    // 2. 구글 로그인 창이 나오고 -> 그중에서 어떤 계정으로 로그인 할지 선택하고 클릭
    // 3. 그러면 로그인이 완료되고 -> 인증에 성공했으므로 code 값을 리턴하게 됨
    // 4. 그러면 oauth2-client 라이브러리가 그 code값을 받아서 Oauth인증 서버에 요청하여 , Oauth 리소스 서버에 접근 가능한 accessToken을 받아옴 => 여기까지가 바로 OAuth2UserRequest 객체 정보이다
    // 5. 이후 그 accessToken이 담긴 OAuth2UserRequest 정보를 Oauth 리소스 서버에 요청하여 -> User리소스 정보를 받아온게 바로 loadUser() 메소드의 역할
    // 그렇담 이 laodUser() 는 누가 호출해주는가? -> OAuth2LoginAuthenticationFilter가 OAuth 인증처리를 수행하는 와중에,
    // 그 안의 필드로 들고 있는 OAuth2LoginAuthenticationProvider에서 OAuth2UserService를 사용하여 리소스 서버로 부터 사용자 정보를 load 한다

    // 해당 함수가 종료될 때, @AuthenticationPrincipal이 만들어 진다
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        /**
         * [registrationId] : 어떤 Oauth Server로 로그인 했는지 확인 가능
         * */
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Provider provider = Provider.convert(registrationId);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributeMap = oAuth2User.getAttributes();
        System.out.println("*** client_id : " + registrationId);
        System.out.println("*** attributeMap : " + attributeMap);


        User authenticatedUser = userRepository.findByUsernameAndEntityStatus(provider + "_" + provider.getProviderId(attributeMap),
                                                                  EntityStatus.ACTIVE).orElseGet(() -> {

            User user = User.createAtOAuth2(provider + "_" + provider.getProviderId(attributeMap),
                                            "1",
                                            Authority.USER,
                                            Role.ROLE_USER,
                                            provider.getEmail(attributeMap),
                                            provider.getName(attributeMap),
                                            provider,
                                            provider.getProviderId(attributeMap));

            userRepository.save(user);
            return user;
        });

        return PrincipalDetails.convertAtOAuthLogin(authenticatedUser, attributeMap);
    }
}
