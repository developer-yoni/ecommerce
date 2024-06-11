package com.example.ecommerce.global.util;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest
class SlackAdaptorTest {

    @Autowired
    SlackAdaptor slackAdaptor;

    @Test
    @DisplayName("슬랙으로 메세지가 가는지 확인")
    void 슬랙으로_메세지가_가는지_확인() throws Exception {

        //given
        //when
        //then
        // 비동기 작업 완료를 기다리기 위한 CountDownLatch

        LocalDateTime startDateTime = LocalDateTime.now();
        slackAdaptor.sendMessage("테스트6", "메시지6");
        LocalDateTime endDateTime = LocalDateTime.now();

        Thread.sleep(5000);
        log.info("수행 시간 : {}", Duration.between(startDateTime, endDateTime).toMillis());
    }
}