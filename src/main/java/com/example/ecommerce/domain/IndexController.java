package com.example.ecommerce.domain;

import com.example.ecommerce.domain.user.dto.request.UserCreateRequestDto;
import com.example.ecommerce.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/user")
    @ResponseBody
    public String user() {

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
}
