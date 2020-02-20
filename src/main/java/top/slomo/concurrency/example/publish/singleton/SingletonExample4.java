package top.slomo.concurrency.example.publish.singleton;

import lombok.extern.slf4j.Slf4j;
import top.slomo.concurrency.annotations.NotThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 懒汉模式 -> 双重同步锁单例模式
 * 单例实例在第一次使用的时候进行创建
 */
@Slf4j
@NotThreadSafe
public class SingletonExample4 {

    // 私有构造函数
    private SingletonExample4() {}

    // 1. memory = allocate() 分配对象内存空间
    // 2. ctorInstance() 初始化对象
    // 3. instance = memory 设置instance指向刚分配的内存

    // JVM和CPU优化发生了指令重排

    // 1. memory = allocate() 分配对象内存空间
    // 3. instance = memory 设置instance指向刚分配的内存
    // 2. ctorInstance() 初始化对象
    // 这时A线程正在创建对象, 指令重排位 1-3-2, 此时走到3
    // B线程同时进行第一次instance == null 判断, 发现instance不等于null, 直接返回了instance
    // 而此时线程A还没有对对象进行初始化, 线程B拿到的是一个空对象, 所以线程不安全

    // 单例对象
    private static SingletonExample4 instance = null;

    // 静态的工厂方法
    public static SingletonExample4 getInstance() {

        if (instance == null) { // 双重检测机制
            synchronized (SingletonExample4.class) { // 同步锁
                if (instance == null) {
                    log.info("SingletonExample1 created");
                    instance = new SingletonExample4();
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
