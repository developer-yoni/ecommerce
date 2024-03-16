package com.example.ecommerce.domain.market.facade;

import com.example.ecommerce.domain.market.Market;
import com.example.ecommerce.domain.market.repository.MarketRepository;
import com.example.ecommerce.domain.stock.service.StockService;
import com.example.ecommerce.global.enums.EntityStatus;
import com.example.ecommerce.global.response.ApiCode;
import com.example.ecommerce.global.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PessimisticLockMarketStockFacade {

    private final MarketRepository marketRepository;
    private final StockService stockService;

    @Transactional
    public void decrease(Long marketId, Long stockId, Long quantity) throws InterruptedException {

        //1. marketId로 PessimisticLock을 걸고
        Market market = marketRepository.findByIdAndEntityStatusWithPessimisticLock(marketId, EntityStatus.ACTIVE).orElseThrow(() -> {
            throw new ApiException(ApiCode.CODE_000_0011, "market 조회 실패");
        });

        //2. stockId로 stock의 inventoryQuantity 감소
        stockService.decreaseForRequiresNewTransaction(stockId, quantity);
    }
}
