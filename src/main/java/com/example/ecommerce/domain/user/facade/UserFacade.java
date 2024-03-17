package com.example.ecommerce.domain.user.facade;

import static com.example.ecommerce.global.response.ApiCode.CODE_000_0014;

import com.example.ecommerce.domain.user.dto.response.UserNicknameResponseDto;
import com.example.ecommerce.domain.user.service.UserService;
import com.example.ecommerce.global.async.AsyncTaskDelegater;
import com.example.ecommerce.global.response.ApiCode;
import com.example.ecommerce.global.response.ApiException;
import com.example.ecommerce.global.response.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserFacade {

    private final UserService userService;
    private final AsyncTaskDelegater asyncTaskDelegater;

    public List<UserNicknameResponseDto> editNickname(List<Long> userIdList) {

        List<UserNicknameResponseDto> userNicknameResponseDtoList = userIdList.stream()
                                                                              .map(userService::editNickname)
                                                                              .collect(Collectors.toList());

        return userNicknameResponseDtoList;
    }

    public ApiResponse<List<UserNicknameResponseDto>> editNicknameAsync(List<Long> userIdList) {

        AtomicInteger failResultCount = new AtomicInteger(0);

        //1. 각 userId별로 각 쓰레드가 닉네임을 변경한다
        List<Future<UserNicknameResponseDto>> futureList = userIdList.stream()
                                                                                 .map(asyncTaskDelegater::editNicknameAsync)
                                                                                 .collect(Collectors.toList());

        //2. Future.get()을 통해 모든 쓰레드가 변경을 완료할 때 까지 기다린 후, 그 결과를 집계한다.
        List<UserNicknameResponseDto> resultList = new ArrayList<>();
        for (Future<UserNicknameResponseDto> future : futureList) {

            try {

                UserNicknameResponseDto userNicknameResponseDto = future.get(10, TimeUnit.SECONDS);
                resultList.add(userNicknameResponseDto);
            } catch (InterruptedException e) {

                // 스레드의 인터럽트 상태를 복원
                Thread.currentThread().interrupt();
                log.error("error = {}", e, e);
            } catch (ExecutionException e) {

                Throwable cause = e.getCause();
                if (cause instanceof ApiException) {

                    failResultCount.incrementAndGet();
                }
                log.error("error = {} , message = {}", e.getClass().toString(), e.getMessage() ,e);
            } catch (TimeoutException e) {

                log.error("error = {} , message = {}", e.getClass().toString(), e.getMessage() ,e);
            }
        }

        //3. 이후 변경에 실패한 개수에 따라, [전체 실패] / [부분 성공] / [전체 성공]을 구분하여 응답한다

        // [전체 실패]
        if (failResultCount.get() == userIdList.size()) {

            return ApiResponse.success(ApiCode.CODE_000_0004, resultList);
        }

        // [부분 성공]
        if (failResultCount.get() > 0) {

            return ApiResponse.success(ApiCode.CODE_000_0003, resultList);
        }

        // [전체 성공]
        return ApiResponse.success(ApiCode.CODE_000_0002, resultList);
    }

    public List<UserNicknameResponseDto> editNicknameOnError(List<Long> userIdList) {

        //1. 일부로 예외를 발생시켜 -> 예외가 발생하지 않고 성공적으로 닉네임이 변경된 경우에 한하여 , 응답을 모음.
        List<UserNicknameResponseDto> userNicknameResponseDtoList = userIdList.stream()
                                                                              .map(userId -> {
                                                                                  try {

                                                                                      return userService.editNicknameOnError(userId);
                                                                                  } catch (ApiException e) {

                                                                                      if (e.getApiCode().equals(CODE_000_0014)) {

                                                                                          log.error("error = {} , message = {}", e.getClass().toString(), e.getMessage() ,e);
                                                                                      }
                                                                                      return null;
                                                                                  }
                                                                              })
                                                                              .filter(Objects::nonNull)
                                                                              .collect(Collectors.toList());

        return userNicknameResponseDtoList;
    }

    public ApiResponse<List<UserNicknameResponseDto>> editNicknameAsyncOnError(List<Long> userIdList) {

        AtomicInteger failResultCount = new AtomicInteger(0);

        //1. 각 userId별로 각 쓰레드가 닉네임을 변경한다
        List<Future<UserNicknameResponseDto>> futureList = userIdList.stream()
                                                                     .map(asyncTaskDelegater::editNicknameAsyncOnError)
                                                                     .collect(Collectors.toList());

        //2. Future.get()을 통해 모든 쓰레드가 변경을 완료할 때 까지 기다린 후, 그 결과를 집계한다.
        List<UserNicknameResponseDto> resultList = new ArrayList<>();
        for (Future<UserNicknameResponseDto> future : futureList) {

            try {

                UserNicknameResponseDto userNicknameResponseDto = future.get(10, TimeUnit.SECONDS);
                resultList.add(userNicknameResponseDto);
            } catch (InterruptedException e) {

                // 스레드의 인터럽트 상태를 복원
                Thread.currentThread().interrupt();
                log.error("error = {}", e, e);
            } catch (ExecutionException e) {

                Throwable cause = e.getCause();
                if (cause instanceof ApiException) {

                    failResultCount.incrementAndGet();
                }
                log.error("error = {} , message = {}", e.getClass().toString(), e.getMessage() ,e);
            } catch (TimeoutException e) {

                log.error("error = {} , message = {}", e.getClass().toString(), e.getMessage() ,e);
            }
        }

        //3. 이후 변경에 실패한 개수에 따라, [전체 실패] / [부분 성공] / [전체 성공]을 구분하여 응답한다

        // [전체 실패]
        if (failResultCount.get() == userIdList.size()) {

            return ApiResponse.success(ApiCode.CODE_000_0004, resultList);
        }

        // [부분 성공]
        if (failResultCount.get() > 0) {

            return ApiResponse.success(ApiCode.CODE_000_0003, resultList);
        }

        // [전체 성공]
        return ApiResponse.success(ApiCode.CODE_000_0002, resultList);
    }

    @Transactional
    public void createUserList(String targetNickname) {

        for (int i = 1; i <= 1000; i++) {

            try {

                String username = "username" + i;
                String password = "1234";
                String email = "email" + i + "@gmail.com";
                String nickname = targetNickname + i;
                userService.signUp(username, password, email, nickname);
            } catch (ApiException e) {

                log.error("exception = {}", e.getApiCode(), e);
            }
        }
    }
}
