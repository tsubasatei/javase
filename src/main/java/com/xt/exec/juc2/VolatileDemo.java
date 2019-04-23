package com.xt.exec.juc2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

// MyData.java ==> MyData.class ==> jvm 字节码
class MyData {
    volatile int number;

    public void addTo60() {
        this.number = 60;
    }

    // 此时 number 前面是加了 volatile 关键字修饰的
    public void addPlusPlus() {
        /**
         * n++ 被拆分成3个指令
         * 执行 getField 拿到原始 n
         * 执行iadd进行加1操作
         * 执行putfield写把累加后的值写回
         */
        number++;
    }

    AtomicInteger atomicInteger = new AtomicInteger();
    public void addMyAtomic() {
        atomicInteger.getAndIncrement();
    }
}

/**
 * 1. 验证 volatile 的可见性
 * 1.1 假如 int number = 0; number 变量之前根本没有添加 volatile 关键字修饰, 没有可见性
 * 1.2 添加了 volatile，可以解决可见性问题
 *
 * 2. 验证 volatile 不保证原子性
 * 2.1 原子性指的是什么意思？
 *      不可分割，完整性，也即某个线程正在做某个具体业务时，中间不可以被加塞或者被分割，需要整体完整
 *      要么同时成功，要么同时失败
 *
 * 2.2 volatile 不保证原子性的案例演示
 *
 * 2.3 why：写覆盖，写丢失
 *
 * 2.4 如何解决原子性
 *      * 加 sync
 *      * 使用 juc 下 AtomicInteger
 *
 */
public class VolatileDemo {
    // main 是一切方法的运行入口
    public static void main(String[] args) {
//        seeOkByVolatile();

        MyData myData = new MyData();
        for (int i = 1; i <= 20; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 1000; j++) {
                    myData.addPlusPlus();
                    myData.addMyAtomic();
                }
            }, String.valueOf(i)).start();
        }
        // 需要等待上面20个线程都全部计算完成，再用 main 线程取得最终的结果值看是多少
        while (Thread.activeCount() > 2) {  // 后台默认2个线程：main线程，gc线程
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName() + " int type, \t finally number value: " + myData.number);
        System.out.println(Thread.currentThread().getName() + " atomicInteger type\t finally atomicInteger value: " + myData.atomicInteger);
    }

    /**
     * volatile 可以保证可见性，及时通知其它线程，主物理内存的值已经被修改
     */
    private static void seeOkByVolatile() {
        MyData myData = new MyData(); // 资源类
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t come in");
            try {
                // 暂停一会
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myData.addTo60();
            System.out.println(Thread.currentThread().getName() + "\t updated number value : " + myData.number);
            System.out.println(Thread.currentThread().getName() + "\t updated atomicInteger value : " + myData.atomicInteger);
        }, "AAA").start();


        // 第 2 个线程就是我们的 main 线程
        while (myData.number == 0) {
            // main 线程就一直在这里等待循环，直到 number 值不再等于 0
        }

        System.out.println(Thread.currentThread().getName() + "\t mission is over, main get number value " + myData.number);
    }


}
