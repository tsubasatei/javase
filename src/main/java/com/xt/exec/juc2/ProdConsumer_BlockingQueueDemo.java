package com.xt.exec.juc2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyResource {
    private volatile boolean FLAG = true; // 默认开启，进行生产+消费
    private AtomicInteger atomicInteger = new AtomicInteger();

    private BlockingQueue<String> blockingQueue;

    public MyResource(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }

    public void myProd() throws InterruptedException {
        String data = null;
        boolean retValue;
        while (FLAG) {
            data = atomicInteger.incrementAndGet() + "";
            retValue = blockingQueue.offer(data, 2l, TimeUnit.SECONDS);
            if (retValue) {
                System.out.println(Thread.currentThread().getName() + "\t 插入队列 " + data + " 成功");
            } else {
                System.out.println(Thread.currentThread().getName() + "\t 插入队列 " + data + " 失败");
            }
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName() + "\t 大老板叫停了，表示FLAG=false，生产动作结束");
    }

    public void myConsumer() throws InterruptedException {
        String result = null;
        while (FLAG) {
            result = blockingQueue.poll(2l, TimeUnit.SECONDS);
            if (result == null || result.equalsIgnoreCase("")) {
                FLAG = false;
                System.out.println(Thread.currentThread().getName() + "\t 消费队列超过2秒获取失败，消费退出");
                System.out.println();
                System.out.println();
                return;
            }
            System.out.println(Thread.currentThread().getName() + "\t 消费队列获取" + result + "成功");
        }
    }

    public void stop() {
        this.FLAG = false;
    }
}

/**
 * 生产者消费者3.0
 * volatile/CAS/atomicInteger/BlockingQueue/线程交互/原子引用
 */
public class ProdConsumer_BlockingQueueDemo {
    public static void main(String[] args) {
        MyResource myResource = new MyResource(new ArrayBlockingQueue<>(3));

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "\t 生产线程启动");
                System.out.println();
                myResource.myProd();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "生产者线程").start();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "\t 消费线程启动");
                System.out.println();
                myResource.myConsumer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "消费者线程").start();

        // 暂停一会儿线程
        try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println();
        System.out.println();
        System.out.println("5秒钟时间到，大老板main线程叫停，活动结束");
        myResource.stop();
    }
}
