package com.example.ecommerce.domain.stock.repository;

import com.example.ecommerce.domain.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * 여기서는 편의성을 고려해, Name을 그냥 Stock으로 사용 -> 이러면 사실상 Pessimistic Lock과 큰 흐름 차이가 없게 됨
 * */
public interface LockRepository extends JpaRepository<Stock, Long> {

    @Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
    void getLock(String key);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    void releaseLock(String key);
}
