package top.slomo.concurrency.example.publish.singleton;

import lombok.extern.slf4j.Slf4j;
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
public class SingletonExample6 {

    // 私有构造函数
    private SingletonExample6() {}

    // 单例对象
    private static SingletonExample6 instance = null;

    // 务必将静态代码块放在引用声明之后, 否则会被覆盖
    static {
        instance = new SingletonExample6();
    }

    // 静态的工厂方法
    public static SingletonExample6 getInstance() {
        return instance;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getInstance());
        System.out.println(getInstance());
    }
}
