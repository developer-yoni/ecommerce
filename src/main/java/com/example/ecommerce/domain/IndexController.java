package com.example.ecommerce.domain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/home")
    public String getHome() {

        return "<h1>Home</h1>";
    }
}
