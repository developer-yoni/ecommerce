package com.example.ecommerce.domain.stock.facade;

import com.example.ecommerce.domain.stock.repository.RedisLockRepository;
import com.example.ecommerce.domain.stock.service.StockService;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LettuceLockStockFacade {

    private final RedisLockRepository redisLockRepository;
    private final StockService stockService;


     @Transactional// -> 여기서 트랜잭션으로 묶어버리면 NamedLock과 똑같은 문제! (REQUIRES_NEW 가 아니면)
    // 즉 아직 트랜잭션이 커밋되지 않아 재고 감소가 안되었는데 -> lock을 해제해버리니까 , 감소되지 않은 값을 읽어버려서 여전히 동시성 문제 발생
    public void decrease(Long id, Long quantity) throws InterruptedException {

        AtomicInteger reTryCount = new AtomicInteger(0);

        //1. 100ms 주기로 lock을 얻으려고 계속 시도 -> sleep() 해줘야 redis에 가는 부하를 줄임
        while (!(redisLockRepository.lock(id))) {

            int count = reTryCount.incrementAndGet();
            System.out.println("((((((((((( " + count + " 회 재시도 수행" + " ))))))))))))))");
            Thread.sleep(100);
        }

        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println("Lettuce Lock 획득");

        //2. 이후 lock을 얻고 여기로 오면 -> 재고를 감소시킨다 -> 이후 반드시 락을 해제한다
        try {

            stockService.decreaseForRequiresNewTransaction(id, quantity);
        } finally {

            redisLockRepository.unlock(id);
            System.out.println("Lettuce Lock 반환");
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        }
    }
}
