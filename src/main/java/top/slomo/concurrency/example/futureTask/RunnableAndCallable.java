package top.slomo.concurrency.example.futureTask;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class RunnableAndCallable {

    static class CallableExample implements Callable<String> {

        @Override
        public String call() throws Exception {
            log.info("do something in callable");
            Thread.sleep(5000);
            return "Done";
        }
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Future<String> future = exec.submit(new CallableExample());

        log.info("isDone: {}", future.isDone());

        log.info("isCancelled: {}", future.isCancelled());

        log.info("result: {}", future.get());

        log.info("isDone: {}", future.isDone());

        exec.shutdown();
    }
}
