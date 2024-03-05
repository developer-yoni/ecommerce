package com.example.ecommerce.domain;

import com.example.ecommerce.domain.user.dto.request.UserCreateRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
