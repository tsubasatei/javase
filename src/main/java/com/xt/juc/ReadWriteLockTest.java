package com.xt.juc;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteLock: 读写锁
 *
 * 写写/读写：需要"互斥"
 * 读读： 不需要"互斥"
 */
public class ReadWriteLockTest {

    public static void main(String[] args) {
        ReadWriteLockDemo rwl = new ReadWriteLockDemo();

        new Thread(() -> rwl.set((int)(Math.random() * 101)), "写: ").start();

        for (int i = 0; i < 500; i++) {
            new Thread(() -> rwl.get(), "读: ").start();
        }
    }
}

class ReadWriteLockDemo {
    private int number = 0;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    // 读
    public void get() {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + ":" + number);
        } finally {
            lock.readLock().unlock();
        }
    }

    // 写
    public void set(int number) {
        lock.writeLock().lock();
        try {
            this.number = number;
            System.out.println(Thread.currentThread().getName() + ":" + number);
        } finally {
            lock.writeLock().unlock();
        }
    }

}