package top.slomo.concurrency.example.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.StampedLock;

public class StampLockSample {
    class Point {
        private double x;
        private double y;
        private final StampedLock lock = new StampedLock();

        // 移动, 写操作
        void move(double deltaX, double deltaY) {
            // 获取写锁
            long stamp = lock.writeLock();
            try {
                x += deltaX;
                y += deltaY;
            } finally {
                lock.unlockWrite(stamp);
            }
        }

        // 乐观读
        double distanceFromOrigin() {
            // 尝试乐观读锁
            long stamp = lock.tryOptimisticRead();
            double currentX = x, currentY = y;
            if (!lock.validate(stamp)) { // 如果加乐观读锁失败
                stamp = lock.readLock(); // 获取悲观读锁
                try {
                    currentX = x;
                    currentY = y;
                } finally {
                    lock.unlockWrite(stamp);
                }
            }

            return Math.sqrt(currentX * currentX + currentY + currentY);
        }

        // 悲观读
        void moveIfAtOrigin(double newX, double newY) {
            // 获取读锁
            long stamp = lock.readLock();
            try {
                while (x == 0 && y == 0) {
                    long ws = lock.tryConvertToWriteLock(stamp);
                    if (ws != 0L) { // 转换写锁成功
                        stamp = ws;
                        x = newX;
                        y = newY;
                        break;
                    } else { // 转换不成功
                        // 显示释放读锁
                        lock.unlock(stamp);
                        // 获取写锁
                        stamp = lock.writeLock();
                    }
                }
            } finally {
                lock.unlock(stamp);
            }
        }
    }
}
