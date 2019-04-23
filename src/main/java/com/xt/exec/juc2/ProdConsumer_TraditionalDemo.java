package com.xt.exec.juc2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShareData {
    private volatile int number;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void increment() {
        lock.lock();
        try {
            while (number != 0) {
                condition.await();
            }
            number++;
            System.out.println(Thread.currentThread().getName() + "\t" + number);
            condition.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public void decrement() {
        lock.lock();
        try {
            while (number == 0) {
                condition.await();
            }
            number--;
            System.out.println(Thread.currentThread().getName() + "\t" + number);
            condition.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
/**
 * 题目：一个初始值为0的变量，两个线程对其交替操作，一个加1，一个减1，循环5次
 *
 * 线程操作资源类
 * 干活判断唤醒
 * 严防虚假唤醒机制
 */
public class ProdConsumer_TraditionalDemo {
    public static void main(String[] args) {
        ShareData shareData = new ShareData();
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                shareData.increment();
            }
        }, "AA").start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                shareData.decrement();
            }
        }, "BB").start();
    }
}
