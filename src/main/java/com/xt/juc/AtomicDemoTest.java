package com.xt.juc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一、i++ 的原子性问题： i++ 的操作实际上分为三个步骤"读-改-写"
 *     int i = 10;
 *     i = i++; // 10
 *
 *     int temp = i;
 *     i = i + 1;
 *     i = temp;
 *
 *  二、原子变量：jdk 1.5 后 java.util.concurrent.atomic 包下提供了常用的原子变量：
 *      1. volatile 保证内存可见性
 *      2。 CAS (Compare-And-Swap) 算法保证数据的原子性
 *         CAS 算法的硬件对于并发操作共享数据的支持
 *         CAS 包含了三个操作数：
 *         内存值 V
 *         预估值 A
 *         更新值 B
 *         当且仅当 V == A 时，V = B。否则，将不做任何操作
 */
public class AtomicDemoTest {
    public static void main(String[] args) {
        AtomicDemo demo = new AtomicDemo();
        for (int i = 0; i < 10; i++) {
            new Thread(demo).start();
        }
    }
}

class AtomicDemo implements Runnable {
    private AtomicInteger serialNum = new AtomicInteger();
    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(getSerialNum());
    }

    public Integer getSerialNum() {
        return serialNum.getAndIncrement();
    }
}
