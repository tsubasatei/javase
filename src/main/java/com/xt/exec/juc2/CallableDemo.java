package com.xt.exec.juc2;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

class MyThread implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName() + "********* come in Callable");
        return 1024;
    }
}
/**
 * 实现线程的第三种方式
 */
public class CallableDemo {
    public static void main(String[] args) throws Exception {

        // 两个线程：一个main主线程，一个AA线程
        // 一个 task 即使2个线程 也只调用一次call
        FutureTask<Integer> futureTask = new FutureTask<>(new MyThread());
        FutureTask<Integer> futureTask2 = new FutureTask<>(new MyThread());
        new Thread(futureTask, "AA").start();
        new Thread(futureTask2, "BB").start();

        System.out.println(Thread.currentThread().getName() + "************");
        int result01 = 100;

        while (!futureTask.isDone()) {

        }
        // 要求获得 Callable 线程的计算结果，如果没有计算完成就要去强求，会导致堵塞，值得计算完成
        Integer result = futureTask.get(); // 建议放在最后
        System.out.println("*************result\t" + (result01 + result));

    }

}
