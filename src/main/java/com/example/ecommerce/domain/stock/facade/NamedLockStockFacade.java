package com.example.ecommerce.domain.stock.facade;

import com.example.ecommerce.domain.stock.Stock;
import com.example.ecommerce.domain.stock.repository.LockRepository;
import com.example.ecommerce.domain.stock.service.StockService;
import com.example.ecommerce.global.enums.EntityStatus;
import com.example.ecommerce.global.response.ApiCode;
import com.example.ecommerce.global.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NamedLockStockFacade {

    private final StockService stockService;
    private final LockRepository lockRepository;

    @Transactional
    public void decrease(Long id, Long quantity) {

        try {

            //1. 락 획득
            lockRepository.getLock(id.toString());
            //2. 재고 감소
            // 이떄 Lock 획득 트랜잭션과 재고 감소 트랜잭션을 분리해야 함
            // 그렇지 않으면 재고감소가 커밋되기 전에, Lock이 해제될 수 있고 -> 그러면 아직 감소되지 않은 값을 읽을 수 있음
            // 따라서 트랜잭션을 분리히하여 , 락을 해제할 땐 반으시 업데이트를 반영한 후 -> 라는 사실을 보장해야 함
            stockService.decreaseForRequiresNewTransaction(id, quantity);
        } finally {

            //3. 명시적으로 락 해제
            lockRepository.releaseLock(id.toString());
        }
    }
}
