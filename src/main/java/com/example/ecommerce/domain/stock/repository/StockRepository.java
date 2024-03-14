package com.example.ecommerce.domain.stock.repository;

import com.example.ecommerce.domain.stock.Stock;
import com.example.ecommerce.global.enums.EntityStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByIdAndEntityStatus(Long id, EntityStatus entityStatus);
}
