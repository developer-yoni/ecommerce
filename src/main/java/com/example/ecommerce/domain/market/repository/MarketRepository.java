package com.example.ecommerce.domain.market.repository;

import com.example.ecommerce.domain.market.Market;
import com.example.ecommerce.domain.stock.Stock;
import com.example.ecommerce.global.enums.EntityStatus;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface MarketRepository extends JpaRepository<Market, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Market m where m.id=:id and m.entityStatus=:entityStatus")
    Optional<Market> findByIdAndEntityStatusWithPessimisticLock(Long id, EntityStatus entityStatus);
}
