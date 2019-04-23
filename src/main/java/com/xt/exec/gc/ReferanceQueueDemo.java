package com.xt.exec.gc;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * 引用队列
 */
public class ReferanceQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        Object obj = new Object();
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        WeakReference<Object> weakReference = new WeakReference<>(obj, referenceQueue);

        System.out.println(obj);
        System.out.println(weakReference.get());
        System.out.println(referenceQueue.poll());

        System.out.println("==========");
        obj = null;
        System.gc();
        Thread.sleep(500);

        System.out.println(obj);
        System.out.println(weakReference.get());
        System.out.println(referenceQueue.poll());

    }
}
