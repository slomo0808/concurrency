package top.slomo.concurrency.example.threadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadPoolExample4 {
    public static void main(String[] args) {

        ScheduledExecutorService exec = Executors.newScheduledThreadPool(3);

//        exec.schedule(
//                ()->log.info("execute"),
//                3, TimeUnit.SECONDS);

        exec.scheduleAtFixedRate(
                ()->log.info("execute"),
                1,
                3,
                TimeUnit.SECONDS
        );
        // exec.shutdown();
    }
}
