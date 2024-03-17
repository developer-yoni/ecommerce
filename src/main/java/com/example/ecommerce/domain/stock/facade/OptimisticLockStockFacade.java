package com.example.ecommerce.domain.stock.facade;

import com.example.ecommerce.domain.stock.service.OptimisticLockStockService;
import com.example.ecommerce.global.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OptimisticLockStockFacade {

    private final OptimisticLockStockService optimisticLockStockService;

    /**
     * [@Transactional을 붙이면 무한루프 빠짐]
     * : 재시도 로직까지 하나의 트랜잭션으로 묶이게 되면서 -> Entity Manager가 비워지지 않아 -> 이전에 실패한 version의 Stock을 가지고 계속 시도하니 , 무한루프에 빠진다
     * */
    //@Transactional
    public void decrease(Long id, Long quantity) throws InterruptedException {

        // update 실패시 재시도
        while (true) {

            try {

                optimisticLockStockService.decrease(id, quantity);
                break;
            }
            catch (Exception e) {

                System.out.println("@@@@@@@@@@@@@@@@@@@");
                System.out.println(e.getClass().getName());
                System.out.println("@@@@@@@@@@@@@@@@@@@");

                if (e instanceof ApiException) {

                    // 재고 문제로 발생하는 경우 (update version 차이가 아니라) 원래 실패 이므로 -> 재시도 하지 않음
                    /** 단 이 경우, ApiException이 상위 Layer까지 전파되지 않는 문제 발생
                     * 이 경우는 Exception catch 문 안에서 분기처리 하여 ApiException인 경우 다시 throw new ApiException을 새줘야 할 텐데 , 그코드 자체로 적절한지는 모르겠음 */
                    throw new ApiException(((ApiException) e).getApiCode(), ((ApiException) e).getInternalMessage());
                }
                // 여기서 Optimistic Lock의 version 차이로 인한 update 실패시 까지 잡아야 하므로 ApiException만 명시하면 안됨
                // 단 재고 수량 0 또는 재고보다 더 많은 요청이 올 경우 -> ApiException이 터지는데 -> 그 또한 여기서 잡아서 재시도 하므로 -> 이 예외 처리를 조심해서 안하면 무한루프 빠짐
                //Thread.sleep(100);
            }
        }
    }
}
