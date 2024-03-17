package com.example.ecommerce.domain.stock.service;

import com.example.ecommerce.domain.stock.Stock;
import com.example.ecommerce.domain.stock.repository.StockRepository;
import com.example.ecommerce.global.enums.EntityStatus;
import com.example.ecommerce.global.response.ApiCode;
import com.example.ecommerce.global.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OptimisticLockStockService {

    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long id, Long quantity) {

        //1. stock 조회
        Stock stock = stockRepository.findByIdAndEntityStatusWithOptimisticLock(id, EntityStatus.ACTIVE)
                                     .orElseThrow(() -> new ApiException(ApiCode.CODE_000_0011, "재고 감소시, 요청값으로 들어온 stockId로 Stock 조회 실패"));

        //2. 재고 감소
        stock.decreaseInventoryQuantity(quantity);

        //3. 갱신된 값을 저장
        stockRepository.saveAndFlush(stock);
    }
}
