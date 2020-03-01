package top.slomo.concurrency.example.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class LockExample6 {

    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();


        new Thread(() -> {
            reentrantLock.lock();
            try {
                log.info("1 wait signal"); // 1
                condition.await();
                log.info("1 get signal"); // 4
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {

                reentrantLock.unlock();
            }
        }).start();

        /*new Thread(() -> {
            reentrantLock.lock();
            try {
                log.info("2 wait signal"); // 1
                condition.await();
                log.info("2 get signal"); // 4
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }).start();*/

        new Thread(() -> {
            reentrantLock.lock();
            log.info("get lock"); // 2
            try {
                Thread.sleep(3000);
                condition.signalAll();
                log.info("send signal ~"); // 3
            }catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }).start();


    }
}
