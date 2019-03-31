package com.xt.thread;

import java.util.concurrent.*;

/**
 * jdk 5.0 创建线程：
 * 1. 实现 Callable 接口
 * 2. 使用线程池
 * 好处：
 *  提高响应速度 （减少了创建新线程的时间）
 *  降低资源消耗 （重复利用线程池中，不需要每次都创建）
 *  便于线程管理
 *  corePoolSize ：核心池的 大小
 *  maximumPoolSize ：最大线程 数
 *  keepAliveTime ：线程没有任务时最多保持长间后会 终止
 *
 */

// 1. 创建一个实现 Callable 接口的实现类
class MyCallable implements Callable<Integer> {
    // 2. 实现 call 方法，将此线程需要执行的操作声明在 call() 中
    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for(int i = 0; i <= 100; i++) {
            if (i % 2 == 0) {
                System.out.println(Thread.currentThread().getName() + " : " + i);
                sum += i;
            }
        }
        return sum;
    }
}
public class ThreadTest {

    public static void main(String[] args) {

        // 3. 创建 Callable 接口的实现类对象
        MyCallable myCallable = new MyCallable();
        // 4. 将此 Callable 接口实现类的对象作为传递到 FutureTask 构造器中，创建 FutureTask 的对象
        FutureTask<Integer> futureTask = new FutureTask<>(myCallable);
        // 5. 将 FutureTask 的对象作为参数传递到 Thread 类的构造器中，创建 Thread 对象，并调用 start() 方法
        Thread t1 = new Thread(futureTask);
        t1.start();

        Integer sum = null;
        try {
            // 6 获取 Callable 的 call() 方法的返回值
            // get() 返回值即为 FutureTask 构造器参数 Callable 实现类重写的 call() 的返回值。
            sum = futureTask.get();
            System.out.println("总和为： " + sum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("-------------");

        // 1. 提供指定线程数量的线程池
//        ExecutorService service = Executors.newFixedThreadPool(10);
        ThreadPoolExecutor service = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        // 设置线程池的属性
        System.out.println(service.getClass());
        service.setCorePoolSize(15);
        service.setKeepAliveTime(3000l, null);
        // execute() 适用于 Runnable
        // 2. 执行指定的线程的操作，需要提供实现 Runnable接口或 Callable 接口实现类的对象
        service.execute(() -> {
            for(int i = 0; i <= 100; i++) {
                if (i % 2 == 0) {
                    System.out.println(Thread.currentThread().getName() + " : " + i);
                }
            }
        });

        service.execute(() -> {
            for(int i = 0; i <= 100; i++) {
                if (i % 2 != 0) {
                    System.out.println(Thread.currentThread().getName() + " : " + i);
                }
            }
        });

        Future<Integer> submit = service.submit(new MyCallable());
        try {
            System.out.println(submit.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // 3. 关闭连接池
        service.shutdown();
    }
}
