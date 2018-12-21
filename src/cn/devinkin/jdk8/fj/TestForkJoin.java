package cn.devinkin.jdk8.fj;


import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

public class TestForkJoin {

    @Test
    public void test1() {
        Instant start = Instant.now();
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Long> task = new ForkJoinCaculation(0, 50000000L);
        Long sum = pool.invoke(task);
        System.out.println(sum);
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end).toMillis() + " ms");
    }

    @Test
    public void test2() {
        Instant start = Instant.now();
        long sum = 0;
        for (long i = 0; i <= 500000000L; i++) {
            sum += i;
        }
        System.out.println(sum);
        Instant end = Instant.now();
        System.out.println(Duration.between(start,end).toMillis() + " ms");
    }


    /**
     * java8并行流
     */
    @Test
    public void test3() {
        Instant start = Instant.now();
        long sum = LongStream.rangeClosed(0, 1000000000L)
                .parallel()
                .reduce(0, Long::sum);
        System.out.println(sum);
        Instant end = Instant.now();
        System.out.println(Duration.between(start,end).toMillis() + " ms");
    }
}

