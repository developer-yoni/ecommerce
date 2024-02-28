package com.example.ecommerce.domain;

import com.example.ecommerce.domain.user.dto.request.UserCreateRequestDto;
import com.example.ecommerce.domain.user.service.UserService;
import com.example.ecommerce.global.auth.PrincipalDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserService userService;

    @GetMapping(value = "/")
    //@ResponseBody
    public String index() {

        return "index";
    }

    /**
     * form login을 하든, oauth2 login을 하든, PrincipalDetails로 받을 수 있음
     * */
    @GetMapping("/user")
    @ResponseBody
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        System.out.println("**************");
        System.out.println("user : " + principalDetails.getUser().getUsername());
        return "유저 페이지입니다.";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {

        return "어드민 페이지입니다.";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager() {

        return "매니저 페이지입니다.";
    }

    //SecurityConfig 파일 생성 후 -> 작동 안함
    @GetMapping("/loginForm")
    public String getLoginForm() {

        return "loginForm";
    }

    @PostMapping("/login")
    public String login() {

        return "loginForm";
    }


    @GetMapping("/joinForm")
    public String getJoinForm() {

        return "joinForm";
    }

    @PostMapping("/join")
    public String join(@Valid  @ModelAttribute UserCreateRequestDto userCreateRequestDto) {

        userService.signUp(userCreateRequestDto.getUsername(), userCreateRequestDto.getPassword(), userCreateRequestDto.getEmail());
        return "redirect:/loginForm";
    }

    @GetMapping("/info")
    @ResponseBody
    @Secured(value = {"MANAGER", "ADMIN"}) // 메소드 실행 전에 권한 검사 수행 -> 단 SPEL등은 사용 못하는 등 세밀한 제어는 불가
    public String getInfo() {

        return "개인정보";
    }

    @GetMapping("/data")
    @ResponseBody
    @PreAuthorize(value = "hasAuthority('MANAGER') or hasAuthority('ADMIN')") // 메소드 실행 전에 권한 검사 수행 -> SPEL등도 사용 가능 등 세밀한 제어 가능
    public String getData() {

        return "데이터 정보";
    }

    /**
     * 1. ArgumentResolver에 의해 인증에 성공한 Authentication을 직접 파라미터로 받을 수도 있고
     * 2. Security에서 구현한 AuthenticationPrincipalArugumentResolver에 의해 @AuthenticationPrincipal을 쓰면 직접 UserDetails 구현체를 Controller의 파라미터로 받을 수 도 있음
     * */
    @GetMapping("/test/login")
    @ResponseBody
    @PreAuthorize(value = "hasAnyAuthority('USER', 'MANAGER', 'ADMIN')")
    public String testLogin(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        System.out.println("**************************");
        System.out.println("authentication : " + principalDetails.getUser().getUsername());
        return "세선졍보 확인하기";
    }

    /**
     * 1. ArgumentResolver에 의해 인증에 성공한 Authentication을 에서 Principal구현체를 꺼낸 다음에 -> 이를 OAuth2User 타입으로 casting 하여 사용할 수도 있고
     * 2. Security에서 구현한 AuthenticationPrincipalArugumentResolver에 의해 @AuthenticationPrincipal을 쓰면 직접 OAuth2User 구현체를 Controller의 파라미터로 받을 수 도 있음
     * */
    @GetMapping("/test/oauth/login")
    @ResponseBody
    @PreAuthorize(value = "hasAnyAuthority('USER', 'MANAGER', 'ADMIN')")
    public String testOAuthLogin(@AuthenticationPrincipal OAuth2User oAuth2User) {

        System.out.println("**************************");
        System.out.println("authentication : " + (oAuth2User.getAttribute("email")));
        return "세선졍보 확인하기";
    }
}
