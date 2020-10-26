package top.slomo.concurrency.example.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockExampleCondition {
    private final static ReentrantLock lock = new ReentrantLock();
    private final static Condition condition = lock.newCondition();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + "获取到锁");
            try {
                System.out.println(Thread.currentThread().getName() + "放弃锁进入await状态");
                condition.await();
                System.out.println(Thread.currentThread().getName() + "执行完成");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + "获取到锁");
            System.out.println(Thread.currentThread().getName() + "唤醒其他线程");
            condition.signal();
            try {
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName() + "执行完成");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t2");

        t1.start();
        t2.start();

    }
}
