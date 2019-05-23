package com.xt.mytest.juc;

/**
 * 死锁
 */
class DeadLock{
    private String lockA;
    private String lockB;

    public DeadLock(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    public void lock(String lockA, String lockB) {
        synchronized (lockA) {
            System.out.println("拥有锁" + lockA);
            synchronized (lockB) {
                System.out.println();
            }
        }
    }
}
public class DeadLockDemo {

    public static void main(String[] args) {
        String lockA = "lockA";
        String lockB = "lockB";
        DeadLock deadLock = new DeadLock(lockA, lockB);
        new Thread(() -> {
            deadLock.lock(lockA, lockB);
        }, "AA").start();
        new Thread(() -> {
            deadLock.lock(lockB, lockA);
        }, "BB").start();

    }

}
