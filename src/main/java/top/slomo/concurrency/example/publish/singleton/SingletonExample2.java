package top.slomo.concurrency.example.publish.singleton;

import lombok.extern.slf4j.Slf4j;
import top.slomo.concurrency.annotations.NotThreadSafe;
import top.slomo.concurrency.annotations.ThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 饿汉模式
 * 单例实例在类装载时进行创建
 *
 * 如果构造方法较为复杂, 可能使得类加载慢, 造成性能问题
 *
 * 如果只进行加载, 而没有调用的话, 会造成资源的浪费
 */
@Slf4j
@ThreadSafe
public class SingletonExample2 {

    // 私有构造函数
    private SingletonExample2() {}

    // 单例对象
    private static SingletonExample2 instance = new SingletonExample2();

    // 静态的工厂方法
    public static SingletonExample2 getInstance() {
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
