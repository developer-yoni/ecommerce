package com.example.ecommerce.domain.stock.facade;

import com.example.ecommerce.domain.stock.service.StockService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RedissonLockStockFacade {

    private final RedissonClient redissonClient;
    private final StockService stockService;

    //@Transactional// -> 여기서 트랜잭션으로 묶어버리면 NamedLock과 똑같은 문제! (REQUIRES_NEW 가 아니면)
    // 즉 아직 트랜잭션이 커밋되지 않아 재고 감소가 안되었는데 -> lock을 해제해버리니까 , 감소되지 않은 값을 읽어버려서 여전히 동시성 문제 발생
    public void decrease(Long id, Long quantity) throws InterruptedException {

        //1. 해당 key에 대한 lock을 일단 get (Lock 획득은 아님)
        RLock lock = redissonClient.getLock(id.toString());

        //2.
        try {

            //i) 몇초동안 lock 획득을 시도할 것인지 / 몇초동안 lock을 점유할 것인지 설정하여 -> 비로소 Lock 획득
            // [주의] : lessTime이 지나면 unlock()을 호출하지 않아도 자동으로 lock이 해제된다
            // [주의]
            // - 쓰레드1,2,3,4,5,6,7,8,9,10 중에 쓰레드 1이 lock을 얻으면 쓰레드 2,3,4,5,6,7,8,9,10은 tryLock()안에서 waitTime안까지 대기한다
            // - 이후 쓰레드1이 unlock() 하면 또는 lessTime이 다되서 lock을 반환하면
            // - tryLock()에서 대기하던 쓰레드들 사이에서 경쟁하여 쓰레드2가 lock을 획득한다
            // - 그러면 tryLock()에서 lock을 획득하지 못한 쓰레드 3,4,5,6,7,8,9,10 은 false를 리턴하는게 아니라, 남은 waitTime동안 여전히 대기한다
            // - 이러한 동장은 반복하여 다시 lock이 해제되었을 때 재시도를 하다가 -> 비로소 waitTime 안에 lock을 획득하지 못하면 그때 false를 반환한다
            boolean available = lock.tryLock(20, 1, TimeUnit.SECONDS);

            // ii) 만약 lock 획득에 실패할 경우 로그 남기기
            if (!available) {

                System.out.println("???????????????????????????");
                System.out.println("Redisson Lock 획득 실패");
                System.out.println("???????????????????????????");
                return;
            }

            //iii) 정상적으로 lock을 획득했다면 -> 재고 감소
            stockService.decreaseForRequiresNewTransaction(id, quantity);
        } finally {

            // 마지막엔 항상 lock 해제
            lock.unlock();
        }
    }
}
