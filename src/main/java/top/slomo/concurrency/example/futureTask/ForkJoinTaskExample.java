package top.slomo.concurrency.example.futureTask;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class ForkJoinTaskExample extends RecursiveTask<Integer> {

    private static final int threadhold = 2;
    private int start;
    private int end;

    public ForkJoinTaskExample(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;

        // 如果任务足够小就计算任务
        boolean canCompute = (end - start) <= threadhold;
        if (canCompute) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            // 如果任务大于阈值, 就分裂成两个子任务计算
            int middle = (start + end) / 2;
            ForkJoinTaskExample leftTask = new ForkJoinTaskExample(start, middle);
            ForkJoinTaskExample rightTask = new ForkJoinTaskExample(middle + 1, end);

            // 执行子任务
            leftTask.fork();
            rightTask.fork();

            // 获取结果
            Integer leftResult = leftTask.join();
            Integer rightResult = rightTask.join();

            // 汇总结果
            sum = leftResult + rightResult;
        }
        return sum;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();

        // 生成一个计算任务
        ForkJoinTaskExample forkJoinTaskExample = new ForkJoinTaskExample(1, 100);

        // 执行
        Future<Integer> future = pool.submit(forkJoinTaskExample);

        log.info("result: {}", future.get());

        pool.shutdown();
    }
}
