package com.example.ecommerce.domain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "인덱스 페이지입니다.";
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
    @GetMapping("/login")
    @ResponseBody
    public String login() {

        return "로그인 페이지 입니다";
    }

    @GetMapping("/join")
    @ResponseBody
    public String join() {

        return "회원가입 페이지 입니다";
    }

    @PostMapping("/joinProc")
    @ResponseBody
    public String joinProc() {

        return "회원가입 진행중";
    }
}
