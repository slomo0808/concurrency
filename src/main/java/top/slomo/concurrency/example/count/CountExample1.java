package top.slomo.concurrency.example.count;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.datetime.joda.JodaDateTimeFormatAnnotationFormatterFactory;
import org.springframework.format.datetime.joda.JodaTimeContext;
import top.slomo.concurrency.annotations.NotThreadSafe;

import java.util.concurrent.*;

@Slf4j
@NotThreadSafe
public class CountExample1 {
    // 请求总数
    public static int clientTotal = 5000;

    // 并发线程数
    public static int threadTotal = 200;

    //
    public static int count = 0;
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
        count++;
    }
}
