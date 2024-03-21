package com.example.ecommerce.domain.stock.facade;

import com.example.ecommerce.domain.stock.Stock;
import com.example.ecommerce.domain.stock.repository.StockRepository;
import com.example.ecommerce.domain.stock.service.PessimisticLockStockService;
import com.example.ecommerce.domain.stock.service.StockService;
import com.example.ecommerce.global.enums.EntityStatus;
import com.example.ecommerce.global.response.ApiCode;
import com.example.ecommerce.global.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PessmisticLockFacade {

    private final PessimisticLockStockService pessimisticLockStockService;
    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long id, Long quantity) {

        Stock stock = stockRepository.findByIdAndEntityStatus(id, EntityStatus.ACTIVE).orElseThrow(() -> {
            throw new ApiException(ApiCode.CODE_000_0011, "Stock Not Found");
        });

        pessimisticLockStockService.decreaseForRequiresNewTransaction(id, quantity);
    }
}
