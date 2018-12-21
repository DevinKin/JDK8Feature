package cn.devinkin.jdk8.fj;

import java.util.concurrent.RecursiveTask;

public class ForkJoinCaculation extends RecursiveTask<Long> {
    private long start;
    private long end;

    private static final long THRESHOLD = 10000;

    public ForkJoinCaculation(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long length = end - start;
        if (length <= THRESHOLD) {
            long sum = 0;

            for (long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else {
            long middle = (start + end) / 2;
            ForkJoinCaculation left = new ForkJoinCaculation(start, middle);
            // 拆分子任务, 压入线程队列
            left.fork();
            ForkJoinCaculation right = new ForkJoinCaculation(middle + 1, end);
            // 拆分子任务, 压入线程队列
            right.fork();

            return left.join() + right.join();
        }
    }
}
