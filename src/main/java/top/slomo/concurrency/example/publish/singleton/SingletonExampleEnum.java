package top.slomo.concurrency.example.publish.singleton;

import lombok.extern.slf4j.Slf4j;
import top.slomo.concurrency.annotations.Recommend;
import top.slomo.concurrency.annotations.ThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@ThreadSafe
@Recommend
@Slf4j
public class SingletonExampleEnum {

    // 构造方法
    private SingletonExampleEnum() {
        log.info("singleton created");
    }

    // 工厂函数
    public static SingletonExampleEnum getInstance() {
        return Singleton.INSTANCE.getSingleton();
    }

    // 内部枚举类
    private enum Singleton {
        INSTANCE;

        // 单例
        private SingletonExampleEnum singleton;

        // 枚举的构造函数
        Singleton() {
            singleton = new SingletonExampleEnum();
        }

        // 返回单例对象
        private SingletonExampleEnum getSingleton() {
            return singleton;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        final Semaphore semaphore = new Semaphore(200);

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
