package com.example.ecommerce.domain.stock.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.example.ecommerce.domain.stock.Stock;
import com.example.ecommerce.domain.stock.repository.StockRepository;
import com.example.ecommerce.global.enums.EntityStatus;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Rollback(value = false)
class StockServiceTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    @BeforeEach
    public void beforeEach() {

        Stock stock = Stock.create(1L, 100L);
        stockRepository.save(stock);
    }

    @AfterEach
    public void afterEach() {

        stockRepository.deleteAll();
    }

    @Test
    @DisplayName("재고 감소0. 어떤 동시성 처리도 하지 않은채 단순 감소")
    void 단순1개감소() throws Exception {

        //given
        //when : 100개에서 1개를 감소시키면
        stockService.decrease(1L, 1L);

        //then : 99개가 남는가
        Stock stock = stockRepository.findByIdAndEntityStatus(1L, EntityStatus.ACTIVE).orElseThrow();
        assertThat(stock.getInventoryQuantity()).isEqualTo(99L);
    }

    @Test
    @DisplayName("재고 감소0. 어떤 동시성 처리도 하지 않은채 단순 감소")
    void 동시성처리없이_동시에100개감소() throws Exception {

        //given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        //when : 100개에서 동시에 100개를 감소시키면
        for (int i = 0; i < threadCount; i++) {

            executorService.submit(() -> {

                try {

                    stockService.decrease(1L, 1L);
                } finally {

                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await(); // 모든 쓰레드가 다 리턴할 때까지 대기

        //then : 0개가 남는가
        Stock stock = stockRepository.findByIdAndEntityStatus(1L, EntityStatus.ACTIVE).orElseThrow();
        assertThat(stock.getInventoryQuantity()).isEqualTo(0L);
    }
}