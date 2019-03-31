package com.xt.juc;

import java.time.Duration;
import java.time.Instant;
import java.util.OptionalLong;
import java.util.stream.LongStream;

/**
 * ForkJoinPool 分支/合并框架 / 工作窃取
 */
public class ForkJoinTest {

    // java 8 新特性
    public static void main(String[] args) {
        Instant start = Instant.now();
        OptionalLong reduce = LongStream.rangeClosed(0, 10000000L)
                .parallel().reduce(Long::sum);
        System.out.println(reduce.getAsLong());
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end).toMillis());
    }
}
