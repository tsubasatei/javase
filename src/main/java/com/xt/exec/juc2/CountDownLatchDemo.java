package com.xt.exec.juc2;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch
 */
public class CountDownLatchDemo {
    static CountDownLatch countDownLatch = new CountDownLatch(6);

    public static void main(String[] args) {
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "国，被灭");
                countDownLatch.countDown();
            }, CountryEnum.forEach_CountEnum(i).getRetMessage()).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "\t 秦帝国，一统天下");
        System.out.println(CountryEnum.One);
        System.out.println(CountryEnum.One.getRetCode());
        System.out.println(CountryEnum.One.getRetMessage());
    }

    private static void closeDoor() {


        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t上晚自习后，走人");
                countDownLatch.countDown();
            }, String.valueOf(i)).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "\t**************班长最后关门，走人");

    }
}
