package com.example.ecommerce.global.util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class SlackUtilTest {

    @Test
    @DisplayName("슬랙으로 메세지가 가는지 확인")
    void 슬랙으로_메세지가_가는지_확인() throws Exception {

        //given
        //when
        //then
        try {

            if (LocalDateTime.now().getYear() == 2024) {

                throw new RuntimeException("일부로 발생한 예외");
            }
        } catch (Exception e) {

            SlackUtil.sendMessage("테스트1", e.getMessage());
        }
    }
}