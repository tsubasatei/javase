package com.xt.mytest.juc;

import java.util.concurrent.CountDownLatch;

/**
 * 阻塞当前线程最后执行
 */
public class CountDownLatchTest {

    static CountDownLatch countDownLatch = new CountDownLatch(6);
    public static void main(String[] args) {
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName());
                countDownLatch.countDown();
            }, String.valueOf(i)).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName());
    }
}
