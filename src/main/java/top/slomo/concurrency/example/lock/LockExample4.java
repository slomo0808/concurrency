package top.slomo.concurrency.example.lock;

import lombok.extern.slf4j.Slf4j;
import top.slomo.concurrency.annotations.ThreadSafe;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

@Slf4j
@ThreadSafe
public class LockExample4 {

    class Point {
        private double x, y;
        private final StampedLock sl = new StampedLock();

        void move(double deltaX, double deltaY) { // 独占锁
            long stamp = sl.writeLock(); // 锁定写锁, 获取stamp
            try {
                x += deltaX;
                y += deltaY;
            } finally {
                sl.unlockWrite(stamp); // 解锁, 根据stamp解除对应的锁
            }
        }

        // 乐观锁读取案例
        double distanceFromOrigin() { // 只读方法
            long stamp = sl.tryOptimisticRead(); // 乐观读, 获取stamp
            double currentX = x, currentY = y;
            if (!sl.validate(stamp)) { // 检查发出乐观读锁后, 同时是否有其他写锁发生?
                stamp = sl.readLock(); // 如果有, 我们再次获得一个悲观读锁
                try {
                    currentX = x;
                    currentY = y;
                } finally {
                    sl.unlockRead(stamp);
                }
            }
            return Math.sqrt(currentX * currentX + currentY * currentY);
        }

        // 悲观锁读取案例
        void moveIfAtOrigin(double newX, double newY) { // upgrade
            // Could instead start with optimistic, not read mode
            long stamp = sl.readLock();
            try {
                while (x == 0.0 && y == 0.0) {
                    long ws = sl.tryConvertToWriteLock(stamp); // 将读锁转化成写锁
                    if (ws != 0L) { // 确认转为写锁是否成功
                        stamp = ws; // 成功, 替换票据
                        x = newX;
                        y = newY;
                        break;
                    } else { // 如果不能成功转换成写锁
                        sl.unlockRead(stamp); // 显示释放读锁
                        stamp = sl.writeLock(); // 显示直接进行写锁, 然后循环再试
                    }
                }
            } finally {
                sl.unlock(stamp); // 释放读锁或写锁
            }
        }
    }
}

