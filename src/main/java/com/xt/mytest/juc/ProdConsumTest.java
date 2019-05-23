package com.xt.mytest.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 题目：一个初始值为0的变量，两个线程对其交替操作，一个加1，一个减1，循环5次
 *
 * 线程操作资源类
 * 干活判断唤醒
 * 严防虚假唤醒机制
 */
public class ProdConsumTest {
    public static void main(String[] args) {
        MyProdConsum myProdConsum = new MyProdConsum();
        new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                myProdConsum.prod();
            }
        }, "生产者线程").start();
        new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                myProdConsum.consume();
            }
        }, "消费者线程").start();

    }

}

class MyProdConsum {
    private volatile int number;
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void prod() {
        lock.lock();
        try {
            while (number != 0) {
                condition.await();
            }
            number++;
            System.out.println(Thread.currentThread().getName() + "生产\t" + number);
            condition.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void consume() {
        lock.lock();
        try {
            while (number == 0) {
                condition.await();
            }
            number--;
            System.out.println(Thread.currentThread().getName() + "消费\t" + number);
            condition.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}