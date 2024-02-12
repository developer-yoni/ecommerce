package com.example.ecommerce.domain;

import com.example.ecommerce.domain.user.dto.request.UserCreateRequestDto;
import com.example.ecommerce.domain.user.dto.request.UserNicknameRequestDto;
import com.example.ecommerce.domain.user.facade.UserFacade;
import com.example.ecommerce.domain.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserService userService;
    private final UserFacade userFacade;

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

        userService.signUp(userCreateRequestDto.getUsername(), userCreateRequestDto.getPassword(), userCreateRequestDto.getEmail(), userCreateRequestDto.getNickname());

        return "redirect:/loginForm";
    }

    @PostMapping("/test")
    public ResponseEntity createUser(@Valid @RequestBody UserNicknameRequestDto userNicknameRequestDto) {

        userFacade.createUserList(userNicknameRequestDto.getNickname());
        return ResponseEntity.ok("success");
    }

    @PatchMapping("/test")
    public ResponseEntity editUserNickname() {

        List<Long> userIdList = LongStream.range(1L, 1001L).boxed().collect(Collectors.toList());
        return ResponseEntity.ok(userFacade.editNickname(userIdList));
    }

    @PatchMapping("/test/error")
    public ResponseEntity editUserNicknameOnError() {

        List<Long> userIdList = LongStream.range(1L, 1001L).boxed().collect(Collectors.toList());
        return ResponseEntity.ok(userFacade.editNicknameOnError(userIdList));
    }

    @PatchMapping("/test/async")
    public ResponseEntity editUserNicknameAsync() {

        List<Long> userIdList = LongStream.range(1L, 1001L).boxed().collect(Collectors.toList());
        return ResponseEntity.ok(userFacade.editNicknameAsync(userIdList));
    }

    @PatchMapping("/test/async/error")
    public ResponseEntity editUserNicknameAsyncOnError() {

        List<Long> userIdList = LongStream.range(1L, 1001L).boxed().collect(Collectors.toList());
        return ResponseEntity.ok(userFacade.editNicknameAsyncOnError(userIdList));
    }
}
