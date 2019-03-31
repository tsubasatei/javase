package com.xt.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 一、线程池：提供了一个线程队列，队列中保存着所有等待状态的线程。避免了创建与销毁额外开销，提高了响应的速度。
 *
 * 二、线程池的体系结构：
 *
 *  java.util.concurrent.Executor: 负责线程的使用与调度的根接口
 *    |-- ** ExecutorService 子接口：线程池的主要接口
 *            |-- ThreadPoolExecutor：线程池的实现类
 *            |-- ScheduledExecutorService：子接口，负责线程池的调度
 *                 |-- ScheduledThreadExecutor: 继承 ThreadPoolExecutor, 实现 ScheduledExecutorService
 * 三、工具类：Executors
 * ExecutorService newFixedThreadPool() : 创建固定大小的线程池
 * ExecutorService newCachedThreadPool() : 缓存线程池，线程池的数量不固定，可以根据需求自动的更改数量
 * ExecutorService newSingleThreadExecutor() : 创建单个线程池。线程池中只有一个线程。
 *
 * ScheduledExecutorService newScheduledThreadPool() : 创建延迟或定时的执行任务。
 *
 */
public class ThreadPoolTest {
    public static void main(String[] args) {
        /*

        // 1. 创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(5);

        ThreadPoolDemo tpd = new ThreadPoolDemo();

        // 2. 为线程池中的线程分配任务
        for (int i = 0; i < 10; i++) {
            pool.submit(tpd);
        }

        // 关闭线程池
        pool.shutdown();
        */

        ExecutorService pool = Executors.newCachedThreadPool();

        List<Future> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Future<Integer> future = pool.submit(() -> {
                int sum = 0;
                for (int j = 0; j <= 100; j++) {
                    System.out.println(Thread.currentThread().getName() + " : " + j);
                    sum += j;
                }
                return sum;
            });
            list.add(future);
        }

        pool.shutdown();

        list.stream().forEach(x -> {
            try {
                System.out.println(x.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }
}

class ThreadPoolDemo implements Runnable{
    private int i = 0;

    @Override
    public void run() {
        while (i < 100) {
            System.out.println(Thread.currentThread().getName() + " : " + i++);
        }
    }
}