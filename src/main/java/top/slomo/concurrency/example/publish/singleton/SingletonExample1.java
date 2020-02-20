package top.slomo.concurrency.example.publish.singleton;

import lombok.extern.slf4j.Slf4j;
import top.slomo.concurrency.annotations.NotThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 懒汉模式
 * 单例实例在第一次使用的时候进行创建
 */
@Slf4j
@NotThreadSafe
public class SingletonExample1 {

    // 私有构造函数
    private SingletonExample1() {}

    // 单例对象
    private static SingletonExample1 instance = null;

    // 静态的工厂方法
    // 多线程并发时, 有可能多个线程都取到instance为null, 都去创建新的实例, 然而多个实例不相同, 造成问题
    public static SingletonExample1 getInstance() {

        if (instance == null) {
            log.info("SingletonExample1 created");
            instance = new SingletonExample1();
        }
        return instance;
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();

        final Semaphore semaphore = new Semaphore(20);
        final CountDownLatch countDownLatch = new CountDownLatch(1000);

        for (int i = 0; i < 1000; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    getInstance();
                    semaphore.release();
                }catch (Exception e) {
                    log.error("exception: {}", e);
                }
                countDownLatch.countDown();
            });

        }

        countDownLatch.await();
        executorService.shutdown();

    }
}
