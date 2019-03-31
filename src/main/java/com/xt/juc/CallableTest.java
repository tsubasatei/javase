package com.xt.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 一、创建执行线程的方式三：实现 Callable 接口。相较于实现 Runnable 接口的方式，方法可以有返回值，并且可以抛出异常。
 * 二、执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果。FutureTask 是 Future 接口的实现类。
 */
public class CallableTest {
    public static void main(String[] args) {
        MyThread td = new MyThread();
        // 1. 执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果。
        FutureTask<Integer> task = new FutureTask<>(td);
        new Thread(task).start();
        Integer sum = null;
        try {
            // 接收线程运算后的结果
            sum = task.get(); // FutureTask 可以用于 闭锁
            System.out.println(sum);
            System.out.println("----------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}

class MyThread implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i <= 100; i++) {
            sum += i;
        }
        return sum;
    }
}