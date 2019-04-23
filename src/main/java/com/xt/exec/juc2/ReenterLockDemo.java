package com.xt.exec.juc2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Phone implements Runnable{
    public synchronized void sendSms() {
        System.out.println(Thread.currentThread().getName() + "\t invoked sendSms");
        sendEmail();
    }

    public synchronized void sendEmail() {
        System.out.println(Thread.currentThread().getName() + "\t #####invoked sendEmail");
    }

    Lock lock = new ReentrantLock();
    Lock lock2 = new ReentrantLock();
    @Override
    public void run() {
        get();
    }

    public void get() {
        lock.lock();
        lock.lock();
        lock2.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t invoked get");
            set();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            lock.unlock();
            lock2.unlock();
        }
    }

    public void set() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t ####invoked set");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

/**
 * 可重入锁(递归锁)
 * ReentrantLock
 * Synchronized
 */
public class ReenterLockDemo {
    public static void main(String[] args) {
        Phone phone = new Phone();
        new Thread(() -> {
            phone.sendSms();
        }, "t1").start();
        new Thread(() -> {
            phone.sendSms();
        }, "t2").start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("==================");
        System.out.println("==================");
        System.out.println("==================");
        new Thread(phone, "t3").start();
        new Thread(phone, "t4").start();

    }
}
