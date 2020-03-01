package top.slomo.concurrency.example.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SemaphoreExample4 {
    private static int threadCount = 20;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        final Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            executorService.execute(() -> {
                try {
                    // 尝试获取多个许可
                    if (semaphore.tryAcquire(3, 5, TimeUnit.SECONDS)) {
                        test(threadNum);
                        semaphore.release(3); // 释放一个许可
                    }
                }catch (Exception e) {
                    log.error("exception", e);
                }
            });
        }

        log.info("finish");
        executorService.shutdown();
    }

    private static void test(int i) throws InterruptedException {
        log.info("{}", i);
        Thread.sleep(1000);
    }
}
