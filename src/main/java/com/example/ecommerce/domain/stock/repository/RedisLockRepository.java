package com.example.ecommerce.domain.stock.repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisLockRepository {

    private final RedisTemplate<String, String> redisTemplate;

    // setnx로 lock을 건다
    // 즉 특정 key에 대해 독점적으로 value를 넣는다
    // 성공하면 1 , 실패하면 0 -> 이 응답을 기준으로 lock을 거는것처럼 활용
    public Boolean lock(Long key) {

        return redisTemplate.opsForValue()
                            .setIfAbsent(key.toString(), "lock", Duration.ofMillis(3_000));
    }

    // setnx로 들어간 lock을 제거한다
    // 즉 특정 key에 대한 value를 제거함으로써 -> 이후 setnx로 동일 key에 대한 value를 넣을 수 있게 한다
    public Boolean unlock(Long key) {

        return redisTemplate.delete(key.toString());
    }
}
