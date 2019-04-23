package com.xt.exec.juc2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞队列
 */
public class BlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);

        System.out.println(blockingQueue.offer("a", 2l, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("b", 2l, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("c", 2l, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("x", 2l, TimeUnit.SECONDS)); // false

        System.out.println(blockingQueue.poll(2l, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(2l, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(2l, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(2l, TimeUnit.SECONDS)); // null


        // 阻塞
        /*
        blockingQueue.put("a");
        blockingQueue.put("b");
        blockingQueue.put("c");
        System.out.println("===========");
//        blockingQueue.put("x");

        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
//        System.out.println(blockingQueue.take());
*/
        // 特殊值
        /*System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));
        System.out.println(blockingQueue.offer("x")); //false

        System.out.println(blockingQueue.peek()); // a

        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll()); // null
*/
        // 抛异常
        /*
        System.out.println(blockingQueue.add("a"));
        System.out.println(blockingQueue.add("b"));
        System.out.println(blockingQueue.add("c"));

        System.out.println(blockingQueue.element());

        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
*/
    }
}
