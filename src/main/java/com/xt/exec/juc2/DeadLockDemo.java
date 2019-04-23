package com.xt.exec.juc2;

class HoldLockThread implements Runnable {

    private String lockA;
    private String lockB;

    public HoldLockThread(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {
        synchronized (lockA) {
            System.out.println(Thread.currentThread().getName() + "\t 拥有锁" + lockA + ", 试图获取锁" + lockB);
            synchronized (lockB) {
                System.out.println(Thread.currentThread().getName() + "\t 拥有锁" + lockB + ", 试图获取锁" + lockA);
            }
        }
    }
}
/**
 * 死锁
 */
public class DeadLockDemo {

    public static void main(String[] args) {
        String lockA = "lockA";
        String lockB = "lockB";
        new Thread(new HoldLockThread(lockA, lockB), "AA").start();
        new Thread(new HoldLockThread(lockB, lockA), "BB").start();
    }
}
