package top.slomo.concurrency.example.publish.singleton;

import lombok.extern.slf4j.Slf4j;
import top.slomo.concurrency.annotations.NotThreadSafe;
import top.slomo.concurrency.annotations.ThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 懒汉模式 -> 双重同步锁单例模式
 * 单例实例在第一次使用的时候进行创建
 */
@Slf4j
@ThreadSafe
public class SingletonExample5 {

    // 私有构造函数
    private SingletonExample5() {}

    // 1. memory = allocate() 分配对象内存空间
    // 2. ctorInstance() 初始化对象
    // 3. instance = memory 设置instance指向刚分配的内存


    // 单例对象
    // volatile关键字限制指令重排, 使其线程安全
    private static volatile SingletonExample5 instance = null;

    // 静态的工厂方法
    public static SingletonExample5 getInstance() {

        if (instance == null) { // 双重检测机制
            synchronized (SingletonExample5.class) { // 同步锁
                if (instance == null) {
                    log.info("SingletonExample1 created");
                    instance = new SingletonExample5();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();

        final Semaphore semaphore = new Semaphore(500);
        final CountDownLatch countDownLatch = new CountDownLatch(10000);

        for (int i = 0; i < 10000; i++) {
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
