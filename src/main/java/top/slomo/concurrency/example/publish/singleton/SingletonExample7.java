package top.slomo.concurrency.example.publish.singleton;

import lombok.extern.slf4j.Slf4j;
import top.slomo.concurrency.annotations.Recommend;
import top.slomo.concurrency.annotations.ThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 枚举模式: 最安全
 */
@Slf4j
@ThreadSafe
@Recommend
public class SingletonExample7 {
    // 私有构造函数
    private SingletonExample7() {}


    public static SingletonExample7 getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        INSTANCE;

        private SingletonExample7 singleton;

        // JVM保证这个方法绝对只被调用一次
        Singleton(){
            singleton = new SingletonExample7();
        }

        SingletonExample7 getInstance() {
            return singleton;
        }
    }
}
