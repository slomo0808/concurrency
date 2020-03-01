package top.slomo.concurrency.example.lock;

import lombok.extern.slf4j.Slf4j;
import top.slomo.concurrency.annotations.ThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@ThreadSafe
public class LockExample2 {
    // 请求总数
    public static int clientTotal = 5000;

    // 并发线程数
    public static int threadTotal = 200;

    // 计数
    public static int count = 0;

    // 锁实例
    private final static Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                }catch (Exception e){
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();

        executorService.shutdown();

        log.info("count:{}", count);
    }

    private static void add() {
        lock.lock();
        try {
            count++;
        }finally {
            lock.unlock();
        }
    }
}
