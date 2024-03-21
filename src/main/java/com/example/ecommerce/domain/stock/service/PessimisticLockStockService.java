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
public class PessimisticLockStockService {

    private final StockRepository stockRepository;

    // PessimisticLock은 트랜잭션이 커밋이나 롤백되면 자동 반환되니 -> 하나의 트랜잭션으로 묶는게 중요하다
    @Transactional
    public void decrease(Long id, Long quantity, Long threadNumber) {

        //1. stock 조회
        Stock stock = stockRepository.findByIdAndEntityStatusWithPessimisticLock(id, EntityStatus.ACTIVE)
                                     .orElseThrow(() -> new ApiException(ApiCode.CODE_000_0011, "재고 감소시, 요청값으로 들어온 stockId로 Stock 조회 실패"));

        System.out.println("--------- " + threadNumber + " 번째 쓰레드 락 획득 / 현재 재고 : " + stock.getInventoryQuantity());
        //2. 재고 감소
        stock.decreaseInventoryQuantity(quantity);

        //3. 갱신된 값을 저장
        stockRepository.saveAndFlush(stock);

        // 마지막에 트랜잭션 커밋 되야 -> Pessimistic Lock이 반환된다
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decreaseForRequiresNewTransaction(Long id, Long quantity) {

        //1. stock 조회
        Stock stock = stockRepository.findByIdAndEntityStatusWithPessimisticLock(id, EntityStatus.ACTIVE)
                                     .orElseThrow(() -> new ApiException(ApiCode.CODE_000_0011, "재고 감소시, 요청값으로 들어온 stockId로 Stock 조회 실패"));

        System.out.println("--------- " + Thread.currentThread().getName() + "  쓰레드 락 획득 / 현재 재고 : " + stock.getInventoryQuantity());
        //2. 재고 감소
        stock.decreaseInventoryQuantity(quantity);

        //3. 갱신된 값을 저장
        stockRepository.saveAndFlush(stock);

        // 마지막에 트랜잭션 커밋 되야 -> Pessimistic Lock이 반환된다
    }
}
