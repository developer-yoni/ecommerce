package com.example.ecommerce.global.async;

import com.example.ecommerce.domain.user.dto.response.UserNicknameResponseDto;
import com.example.ecommerce.domain.user.service.UserService;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AsyncTaskDelegater {

    private final UserService userService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CompletableFuture<UserNicknameResponseDto> editNicknameAsync(Long userId) {

        return CompletableFuture.completedFuture(userService.editNickname(userId));
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CompletableFuture<UserNicknameResponseDto> editNicknameAsyncOnError(Long userId) {

        return CompletableFuture.completedFuture(userService.editNicknameOnError(userId));
    }
}
