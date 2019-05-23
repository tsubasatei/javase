package com.xt.mytest.juc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生产者消费者模式-用阻塞队列实现
 */
public class ProdConsume_BlockingQueueTest {
    public static void main(String[] args) {
        MyResource myResource = new MyResource(new ArrayBlockingQueue<>(3));
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 生产线程启动");
            System.out.println();
            myResource.prod();
        }, "生产者线程").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 消费线程启动");
            System.out.println();
            myResource.consume();
        }, "消费者线程").start();

        // 暂停一会儿线程
        try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println();
        System.out.println();
        System.out.println("5秒钟时间到，大老板main线程叫停，活动结束");
        myResource.stop();
    }


}

class MyResource{
    private volatile boolean flag = true;
    private AtomicInteger atomicInteger = new AtomicInteger();
    private BlockingQueue<String> blockingQueue;

    public MyResource(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }

    public void prod() {
        String data = null;
        boolean offer;
        while (flag) {
            data = atomicInteger.incrementAndGet() + "";
            try {
                offer = blockingQueue.offer(data, 2l, TimeUnit.SECONDS);
                if (offer) {
                    System.out.println(Thread.currentThread().getName() + "\t插入数据成功:" + data);
                } else {
                    System.out.println(Thread.currentThread().getName() + "\t插入数据成功:" + data);
                }
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + "\t 大老板叫停了，表示FLAG=false，生产动作结束");
    }

    public void consume() {
        String result = null;
        while (flag) {
            try {
                result = blockingQueue.poll(2l, TimeUnit.SECONDS);
                if (null == result || "".equalsIgnoreCase(result)) {
                    flag = false;
                    System.out.println(Thread.currentThread().getName() + "\t获取数据失败");
                    return;
                }
                System.out.println(Thread.currentThread().getName() + "\t获取数据成功:" + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.flag = false;
    }
}
