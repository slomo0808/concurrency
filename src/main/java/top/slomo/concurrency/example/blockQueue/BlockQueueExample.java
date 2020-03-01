package top.slomo.concurrency.example.blockQueue;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class BlockQueueExample {
    public static void main(String[] args) throws Exception {
        final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                    queue.put(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        for (int i = 0; i < 10; i++) {
            log.info("main thread takes : {}", queue.take());
        }
    }
}
