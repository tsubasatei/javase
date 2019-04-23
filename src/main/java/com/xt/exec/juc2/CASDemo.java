package com.xt.exec.juc2;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS
 * 1. 比较并交换 compareAndSet
 */
public class CASDemo {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);

        System.out.println(atomicInteger.compareAndSet(5, 2019) + "\t current value: " + atomicInteger.get());

        System.out.println(atomicInteger.compareAndSet(5, 2020) + "\t current value: " + atomicInteger.get());
    }
}
