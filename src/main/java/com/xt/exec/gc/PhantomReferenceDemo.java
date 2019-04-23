package com.xt.exec.gc;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * 虚引用
 */
public class PhantomReferenceDemo {
    public static void main(String[] args) throws InterruptedException {
        Object obj = new Object();
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        PhantomReference<Object> weakReference = new PhantomReference<>(obj, referenceQueue);

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
