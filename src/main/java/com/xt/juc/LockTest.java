package com.xt.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一、用于解决线程安全问题的方式：
 *
 * synchronized: 隐式锁
 * 1. 同步代码块
 * 2. 同步方法
 *
 * jdk 1.5 后：
 * 3. 同步锁 Lock
 * 注意：是一个显示锁，需要通过 lock() 方法上锁，必须通过 unlock() 方法进行解锁
 *
 */
public class LockTest {

    public static void main(String[] args) {
        Ticket ticket = new Ticket();
        new Thread(ticket, "1号窗口内").start();
        new Thread(ticket, "2号窗口内").start();
        new Thread(ticket, "3号窗口内").start();
    }
}

class Ticket implements Runnable {

    private int ticket = 100;
    private Lock lock = new ReentrantLock();

    @Override
    public void run() {
        while (true) {
            lock.lock(); // 上锁
            try {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (ticket > 0) {
                    System.out.println(Thread.currentThread().getName() + ": 当前余票为：" + --ticket);
                } else {
                    break;
                }
            } finally {
                lock.unlock();  // 释放锁
            }
        }
    }
}