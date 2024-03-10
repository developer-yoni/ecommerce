package com.example.ecommerce.domain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/home")
    public String getHome() {

        return "<h1>Home</h1>";
    }

    @PostMapping("/login")
    public String signIn() {

        return "<h1>Login 요청</h1>";
    }
}

