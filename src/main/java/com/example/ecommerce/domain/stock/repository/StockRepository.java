package com.example.ecommerce.domain.stock.repository;

import com.example.ecommerce.domain.stock.Stock;
import com.example.ecommerce.global.enums.EntityStatus;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByIdAndEntityStatus(Long id, EntityStatus entityStatus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Stock s where s.id=:id and s.entityStatus=:entityStatus")
    Optional<Stock> findByIdAndEntityStatusWithPessimisticLock(Long id, EntityStatus entityStatus);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select s from Stock s where s.id=:id and s.entityStatus=:entityStatus")
    Optional<Stock> findByIdAndEntityStatusWithOptimisticLock(Long id, EntityStatus entityStatus);
}
