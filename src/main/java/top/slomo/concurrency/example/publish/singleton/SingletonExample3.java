package top.slomo.concurrency.example.publish.singleton;

import lombok.extern.slf4j.Slf4j;
import top.slomo.concurrency.annotations.NotThreadSafe;
import top.slomo.concurrency.annotations.ThreadSafe;
import top.slomo.concurrency.annotations.UnRecommend;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 懒汉模式
 * 单例实例在第一次使用的时候进行创建
 */
@Slf4j
@ThreadSafe
@UnRecommend
public class SingletonExample3 {

    // 私有构造函数
    private SingletonExample3() {}

    // 单例对象
    private static SingletonExample3 instance = null;

    // 静态的工厂方法
    // 添加 synchronized 描述, 一个时间内只允许一个线程访问
    // 虽然线程安全, 但是只允许线程依次访问, 浪费性能
    public static synchronized SingletonExample3 getInstance() {

        if (instance == null) {
            log.info("SingletonExample1 created");
            instance = new SingletonExample3();
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
