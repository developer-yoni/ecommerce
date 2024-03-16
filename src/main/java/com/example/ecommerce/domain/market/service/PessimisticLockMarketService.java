package com.example.ecommerce.domain.market.service;

import com.example.ecommerce.domain.market.Market;
import com.example.ecommerce.domain.market.repository.MarketRepository;
import com.example.ecommerce.domain.stock.Stock;
import com.example.ecommerce.domain.stock.repository.StockRepository;
import com.example.ecommerce.global.enums.EntityStatus;
import com.example.ecommerce.global.response.ApiCode;
import com.example.ecommerce.global.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PessimisticLockMarketService {

    private final MarketRepository marketRepository;
    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long marketId, Long stockId, Long quantity) throws InterruptedException {

        //1. marketId로 PessimisticLock을 걸고
        Market market = marketRepository.findByIdAndEntityStatusWithPessimisticLock(marketId, EntityStatus.ACTIVE).orElseThrow(() -> {
            throw new ApiException(ApiCode.CODE_000_0011, "market 조회 실패");
        });

        //2. stock의 inventoryQuantity 감소
        Stock stock = stockRepository.findByIdAndEntityStatus(stockId, EntityStatus.ACTIVE)
                                     .orElseThrow(() -> new ApiException(ApiCode.CODE_000_0011, "재고 감소시, 요청값으로 들어온 stockId로 Stock 조회 실패"));
        stock.decreaseInventoryQuantity(quantity);

        //3. saveAndFlush
        //stockRepository.saveAndFlush(stock);
        Thread.sleep(1000);
    }
}
