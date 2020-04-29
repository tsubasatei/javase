package com.xt.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程间通信
 * 编写一个程序，开启3 个线程，这三个线程的ID 分别为A、B、C，每个线程将自己的ID 在屏幕上打印10 遍，要求输出的结果必须按顺序显示。
 * 如：ABCABCABC…… 依次递归
 */
public class ABCAlternateTest {
    public static void main(String[] args) {
        AlternateDemo demo = new AlternateDemo();
        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                demo.loopA(i);
            }
        }, "A").start();
        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                demo.loopB(i);
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                demo.loopC(i);
            }
        }, "C").start();
    }
}

class AlternateDemo {
    private int number = 1; // 当前正在执行线程的标记
    private Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();

    public void loopA (int totalNum) {
        lock.lock();
        try {
            // 1. 判断
            if (number != 1) {
                try {
                    condition1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 2. 打印
            for (int j = 1; j <= 1; j++) {
                System.out.println(Thread.currentThread().getName() + "\t\t" + j + "\t\t" + totalNum);
            }
            // 3. 唤醒
            number = 2;
            condition2.signal();
        } finally {
            lock.unlock();
        }
    }

    public void loopB (int totalNum) {
        lock.lock();
        try {
            if (number != 2) {
                try {
                    condition2.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 1; i <= 1; i++) {
                System.out.println(Thread.currentThread().getName() + "\t\t" + i + "\t\t" + totalNum);
            }
            number = 3;
            condition3.signal();
        } finally {
            lock.unlock();
        }
    }

    public void loopC (int totalNum) {
        lock.lock();
        try {
            if (number != 3) {
                try {
                    condition3.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 1; i <= 1; i++) {
                System.out.println(Thread.currentThread().getName() + "\t\t" + i + "\t\t" + totalNum);
                System.out.println("-----------");
            }
            number = 1;
            condition1.signal();
        } finally {
            lock.unlock();
        }
    }
}