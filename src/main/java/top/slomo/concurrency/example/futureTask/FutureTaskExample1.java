package top.slomo.concurrency.example.futureTask;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

@Slf4j
public class FutureTaskExample1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(() -> "Hello World");


        ExecutorService exec = Executors.newCachedThreadPool();

        exec.submit(futureTask);

        log.info("{}", futureTask.get());

        exec.shutdown();
    }
}
