package com.example.ecommerce.domain.stock.service;

import static com.example.ecommerce.global.response.ApiCode.CODE_000_0013;
import static com.example.ecommerce.global.response.ApiCode.CODE_000_0014;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.example.ecommerce.domain.stock.Stock;
import com.example.ecommerce.domain.stock.facade.LettuceLockStockFacade;
import com.example.ecommerce.domain.stock.facade.NamedLockStockFacade;
import com.example.ecommerce.domain.stock.facade.OptimisticLockStockFacade;
import com.example.ecommerce.domain.stock.facade.RedissonLockStockFacade;
import com.example.ecommerce.domain.stock.repository.StockRepository;
import com.example.ecommerce.global.enums.EntityStatus;
import com.example.ecommerce.global.response.ApiException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Rollback(value = false)
class StockServiceTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    @Autowired
    private PessimisticLockStockService pessimisticLockStockService;

    @Autowired
    private OptimisticLockStockFacade optimisticLockStockFacade;

    @Autowired
    private NamedLockStockFacade namedLockStockFacade;

    @Autowired
    private LettuceLockStockFacade lettuceLockStockFacade;

    @Autowired
    private RedissonLockStockFacade redissonLockStockFacade;

    @BeforeEach
    public void beforeEach() {

        Stock stock = Stock.create(1L, 100L);
        stockRepository.save(stock);
    }

    @AfterEach
    public void afterEach() {

        stockRepository.deleteAll();
    }

    @Test
    @DisplayName("재고 감소0. 어떤 동시성 처리도 하지 않은채 1개 단순 감소")
    void 단순1개감소() throws Exception {

        //given
        //when : 100개에서 1개를 감소시키면
        stockService.decrease(1L, 1L);

        //then : 99개가 남는가
        Stock stock = stockRepository.findByIdAndEntityStatus(1L, EntityStatus.ACTIVE).orElseThrow();
        assertThat(stock.getInventoryQuantity()).isEqualTo(99L);
    }

    /**
     * [문제점]
     * 1. 단일서버에만 제한
     * 2. 아니? 심지어 @Transactional AOP 동작으로 인해, 커밋되기 전에 읽어오면 문제 여전히 발생
     * */
    @Test
    @DisplayName("재고 감소1. synchronized로 동시성 처리한 100개 동시 감소")
    void 동시성처리없이_동시에100개감소() throws Exception {

        //given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        long startTime = System.currentTimeMillis(); // 테스트 시작 시간 기록

        //when : 100개에서 동시에 100개를 감소시키면
        for (int i = 0; i < threadCount; i++) {

            executorService.submit(() -> {

                try {

                    stockService.decreaseSynchronized(1L, 1L);
                } catch (ApiException e) {

                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$");
                    if (e.getApiCode().equals(CODE_000_0013)) {

                        System.out.println("Thread Name : " + Thread.currentThread().getName() + " / " + " 남은 재고가 0개 이므로 재고 감소 실패");
                    }

                    if (e.getApiCode().equals(CODE_000_0014)) {

                        System.out.println("Thread Name : " + Thread.currentThread().getName() + " / " + " 남은 재고보다 더 큰 수량만큼 감소 불가");
                    }
                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$");
                } finally {

                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await(); // 모든 쓰레드가 다 리턴할 때까지 대기
        long endTime = System.currentTimeMillis(); // 테스트 종료 시간 기록
        System.out.println("트 : " + (endTime - startTime) + " ms");

        //then : 0개가 남는가
        Stock stock = stockRepository.findByIdAndEntityStatus(1L, EntityStatus.ACTIVE).orElseThrow();
        assertThat(stock.getInventoryQuantity()).isEqualTo(0L);
    }

    @Test
    @DisplayName("재고 감소2_1. Pessimistic Lock으로 동시성 처리 후 100개 동시 감소")
    void PessimisticLock으로_동시성처리후_동시에100개감소() throws Exception {

        //given
        int threadCount = 1;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        long startTime = System.currentTimeMillis(); // 테스트 시작 시간 기록

        //when : 100개에서 동시에 100개를 감소시키면
        for (int i = 0; i < threadCount; i++) {

            executorService.submit(() -> {

                try {

                    pessimisticLockStockService.decrease(1L, 1L);
                } catch (ApiException e) {

                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$");
                    if (e.getApiCode().equals(CODE_000_0013)) {

                        System.out.println("Thread Name : " + Thread.currentThread().getName() + " / " + " 남은 재고가 0개 이므로 재고 감소 실패");
                    }

                    if (e.getApiCode().equals(CODE_000_0014)) {

                        System.out.println("Thread Name : " + Thread.currentThread().getName() + " / " + " 남은 재고보다 더 큰 수량만큼 감소 불가");
                    }
                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$");
                } finally {

                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await(); // 모든 쓰레드가 다 리턴할 때까지 대기
        long endTime = System.currentTimeMillis(); // 테스트 종료 시간 기록
        System.out.println("[테스트 시간] : " + (endTime - startTime) + " ms");

        //then : 0개가 남는가
        Stock stock = stockRepository.findByIdAndEntityStatus(1L, EntityStatus.ACTIVE).orElseThrow();
        assertThat(stock.getInventoryQuantity()).isEqualTo(99L);
    }

    @Test
    @DisplayName("재고 감소2_2. Optimistic Lock으로 동시성 처리 후 100개 동시 감소")
    void OptimisticLock으로_동시성처리후_동시에100개감소() throws Exception {

        //given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        long startTime = System.currentTimeMillis(); // 테스트 시작 시간 기록

        //when : 100개에서 동시에 100개를 감소시키면
        for (int i = 0; i < threadCount; i++) {

            executorService.submit(() -> {

                try {

                    optimisticLockStockFacade.decrease(1L, 1L);
                } catch (ApiException e) {

                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$");
                    if (e.getApiCode().equals(CODE_000_0013)) {

                        System.out.println("Thread Name : " + Thread.currentThread().getName() + " / " + " 남은 재고가 0개 이므로 재고 감소 실패");
                    }

                    if (e.getApiCode().equals(CODE_000_0014)) {

                        System.out.println("Thread Name : " + Thread.currentThread().getName() + " / " + " 남은 재고보다 더 큰 수량만큼 감소 불가");
                    }
                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$");
                } catch (InterruptedException e) {

                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.println("Optimistic Lock을 통한 재시도 요청에서 Thread의 Interrupt Exception 발생");
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
                } finally {

                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await(); // 모든 쓰레드가 다 리턴할 때까지 대기

        long endTime = System.currentTimeMillis(); // 테스트 종료 시간 기록
        System.out.println("[테스트 시간] : " + (endTime - startTime) + " ms");

        //then : 0개가 남는가
        Stock stock = stockRepository.findByIdAndEntityStatus(1L, EntityStatus.ACTIVE).orElseThrow();
        assertThat(stock.getInventoryQuantity()).isEqualTo(0L);
    }

    @Test
    @DisplayName("재고 감소2_3. Named Lock으로 동시성 처리 후 100개 동시 감소")
    void NamedLock으로_동시성처리후_동시에100개감소() throws Exception {

        //given
        int threadCount = 1;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        long startTime = System.currentTimeMillis(); // 테스트 시작 시간 기록

        //when : 100개에서 동시에 100개를 감소시키면
        for (int i = 0; i < threadCount; i++) {

            executorService.submit(() -> {

                try {

                    namedLockStockFacade.decrease(1L, 1L);
                } catch (ApiException e) {

                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$");
                    if (e.getApiCode().equals(CODE_000_0013)) {

                        System.out.println("Thread Name : " + Thread.currentThread().getName() + " / " + " 남은 재고가 0개 이므로 재고 감소 실패");
                    }

                    if (e.getApiCode().equals(CODE_000_0014)) {

                        System.out.println("Thread Name : " + Thread.currentThread().getName() + " / " + " 남은 재고보다 더 큰 수량만큼 감소 불가");
                    }
                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$");
                } finally {

                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await(); // 모든 쓰레드가 다 리턴할 때까지 대기

        long endTime = System.currentTimeMillis(); // 테스트 종료 시간 기록
        System.out.println("[테스트 시간] : " + (endTime - startTime) + " ms");

        //then : 0개가 남는가
        Stock stock = stockRepository.findByIdAndEntityStatus(1L, EntityStatus.ACTIVE).orElseThrow();
        assertThat(stock.getInventoryQuantity()).isEqualTo(99L);
    }

    @Test
    @DisplayName("재고 감소3_1. Lettuce Lock으로 동시성 처리 후 100개 동시 감소")
    void LettuceLock으로_동시성처리후_동시에100개감소() throws Exception {

        //given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        long startTime = System.currentTimeMillis(); // 테스트 시작 시간 기록

        //when : 100개에서 동시에 100개를 감소시키면
        for (int i = 0; i < threadCount; i++) {

            executorService.submit(() -> {

                try {

                    lettuceLockStockFacade.decrease(1L, 1L);
                } catch (ApiException e) {

                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$");
                    if (e.getApiCode().equals(CODE_000_0013)) {

                        System.out.println("Thread Name : " + Thread.currentThread().getName() + " / " + " 남은 재고가 0개 이므로 재고 감소 실패");
                    }

                    if (e.getApiCode().equals(CODE_000_0014)) {

                        System.out.println("Thread Name : " + Thread.currentThread().getName() + " / " + " 남은 재고보다 더 큰 수량만큼 감소 불가");
                    }
                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$");
                } catch (InterruptedException e) {

                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.println("Lettuce Lock을 통한 재시도 요청에서 Thread의 Interrupt Exception 발생");
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
                } finally {

                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await(); // 모든 쓰레드가 다 리턴할 때까지 대기

        long endTime = System.currentTimeMillis(); // 테스트 종료 시간 기록
        System.out.println("[테스트 시간] : " + (endTime - startTime) + " ms");

        //then : 0개가 남는가
        Stock stock = stockRepository.findByIdAndEntityStatus(1L, EntityStatus.ACTIVE).orElseThrow();
        assertThat(stock.getInventoryQuantity()).isEqualTo(0L);
    }

    @Test
    @DisplayName("재고 감소3_2. Redisson Lock으로 동시성 처리 후 100개 동시 감소")
    void RedissonLock으로_동시성처리후_동시에100개감소() throws Exception {

        //given
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        long startTime = System.currentTimeMillis(); // 테스트 시작 시간 기록

        //when : 100개에서 동시에 100개를 감소시키면트
        for (int i = 0; i < threadCount; i++) {

            executorService.submit(() -> {

                try {

                    redissonLockStockFacade.decrease(1L, 1L);
                } catch (ApiException e) {

                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$");
                    if (e.getApiCode().equals(CODE_000_0013)) {

                        System.out.println("Thread Name : " + Thread.currentThread().getName() + " / " + " 남은 재고가 0개 이므로 재고 감소 실패");
                    }

                    if (e.getApiCode().equals(CODE_000_0014)) {

                        System.out.println("Thread Name : " + Thread.currentThread().getName() + " / " + " 남은 재고보다 더 큰 수량만큼 감소 불가");
                    }
                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$");
                } catch (InterruptedException e) {

                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.println("Redisson Lock을 통한 재시도 요청에서 Thread의 Interrupt Exception 발생");
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
                } finally {

                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await(); // 모든 쓰레드가 다 리턴할 때까지 대기

        long endTime = System.currentTimeMillis(); // 테스트 종료 시간 기록
        System.out.println("[테스트 시간] : " + (endTime - startTime) + " ms");

        //then : 0개가 남는가
        Stock stock = stockRepository.findByIdAndEntityStatus(1L, EntityStatus.ACTIVE).orElseThrow();
        assertThat(stock.getInventoryQuantity()).isEqualTo(0L);
    }
}