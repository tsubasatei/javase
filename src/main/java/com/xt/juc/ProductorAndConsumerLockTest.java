package com.xt.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者和消费者案例：使用Lock
 */
public class ProductorAndConsumerLockTest {
    public static void main(String[] args) {
        Clerk1 clerk1 = new Clerk1();
        Productor1 pro = new Productor1(clerk1);
        Consumer1 con1 = new Consumer1(clerk1);

        new Thread(pro, "生产者A").start();
        new Thread(pro, "生产者B").start();
        new Thread(con1, "消费者A").start();
        new Thread(con1, "消费者B").start();
    }
}

class Clerk1 {
    private int product = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    // 进货
    public void get () {
        lock.lock();
        try {
            while (product >= 1) {
                System.out.println("已满");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + " : " + ++product);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    // 销货
    public void sale () {
        lock.lock();
        try {
            while (product <= 0) {
                System.out.println("缺货");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + " : " + --product);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}

// 消费者
class Productor1 implements Runnable {
    private Clerk1 clerk;

    public Productor1(Clerk1 clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            clerk.get();
        }
    }
}

class Consumer1 implements Runnable {
    private Clerk1 clerk;

    public Consumer1(Clerk1 clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            clerk.sale();
        }
    }
}