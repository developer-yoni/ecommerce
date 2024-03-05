package com.example.ecommerce.domain;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @PostMapping("/home")
    public String getHome() {

        String home = "<h1>Home</h1>";
        return home;
    }
}
