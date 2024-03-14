package com.example.ecommerce.domain.stock.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.example.ecommerce.domain.stock.Stock;
import com.example.ecommerce.domain.stock.repository.StockRepository;
import com.example.ecommerce.global.enums.EntityStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
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
    void test() throws Exception {

        //given
        //when : 100개에서 1개를 감소시키면
        stockService.decrease(1L, 1L);

        //then : 99개가 남는가
        Stock stock = stockRepository.findByIdAndEntityStatus(1L, EntityStatus.ACTIVE).orElseThrow();
        assertThat(stock.getInventoryQuantity()).isEqualTo(99L);
    }
}