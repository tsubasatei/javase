package com.xt.exec.gc;

import java.lang.ref.WeakReference;

/**
 * 弱引用
 */
public class WeakReferenceDemo {
    public static void main(String[] args) {
        Object obj = new Object();
        WeakReference<Object> weakReference = new WeakReference<>(obj);
        System.out.println(obj);
        System.out.println(weakReference);

        obj = null;
        System.gc();
        System.out.println(obj);
        System.out.println(weakReference.get());
    }
}
