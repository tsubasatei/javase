package com.xt.exec.juc2;

import java.util.concurrent.*;

/**
 * 第4中获得线程的方式：线程池
 */
public class MyThreadPoolDemo {
    public static void main(String[] args) {

        System.out.println(Runtime.getRuntime().availableProcessors());
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2,
                5,
                1l,
                 TimeUnit.SECONDS,
                 new LinkedBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
//                new ThreadPoolExecutor.AbortPolicy());
//                new ThreadPoolExecutor.CallerRunsPolicy());
//                new ThreadPoolExecutor.DiscardOldestPolicy());
                new ThreadPoolExecutor.DiscardPolicy());
        try {
            for (int i = 1; i <= 10; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t 办理业务");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }

    }

    private static void threadPoolInit() {
        //ExecutorService executorService = Executors.newFixedThreadPool(5); // 一池5个处理线程
//        ExecutorService executorService = Executors.newSingleThreadExecutor(); // 一池1个处理线程
        ExecutorService executorService = Executors.newCachedThreadPool(); // 一池N个处理线程

        try {
            // 模拟10个用户来办理业务，每个用户就是一个来自外部的请求线程
            for (int i = 0; i < 10; i++) {
                executorService.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t 办理业务");
                });
                // 暂停一会儿线程
//                try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}
