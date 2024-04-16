package com.example.ecommerce.domain.user.controller;

import com.example.ecommerce.domain.user.dto.request.UserCreateRequestDto;
import com.example.ecommerce.domain.user.dto.request.UserEditRequestDto;
import com.example.ecommerce.domain.user.dto.response.UserReadResponseDto;
import com.example.ecommerce.domain.user.service.UserService;
import com.example.ecommerce.global.response.ApiCode;
import com.example.ecommerce.global.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ApiResponse<Void> createUser(@Valid @ModelAttribute UserCreateRequestDto userCreateRequestDto) {

        userService.signUp(userCreateRequestDto.getUsername(), userCreateRequestDto.getPassword(), userCreateRequestDto.getEmail());
        return ApiResponse.success(ApiCode.CODE_000_0000);
    }

    @GetMapping("/users")
    public ApiResponse<List<UserReadResponseDto>> getUserList() {

        List<UserReadResponseDto> userReadResponseDtoList = userService.getUserList();
        return ApiResponse.success(ApiCode.CODE_000_0000, userReadResponseDtoList);
    }

    @PatchMapping("/users")
    @PreAuthorize("@userService.hasAuthority(#username)")
    public ApiResponse<Void> editUser(@RequestParam(name = "username")String username,
                                   @Valid @RequestBody UserEditRequestDto userEditRequestDto) {

        userService.editEmail(username, userEditRequestDto.getEmail());
        return ApiResponse.success(ApiCode.CODE_000_0000);
    }
}
