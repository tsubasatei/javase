package com.xt.exec.gc;

import java.lang.ref.SoftReference;

/**
 * 软引用
 * 内存够用时就保留不够用时就回收
 */
public class SoftReferenceDemo {

    public static void softRef_Memory_Enough() {
        Object obj = new Object();
        SoftReference<Object> softReference = new SoftReference<>(obj);
        System.out.println(obj);
        System.out.println(softReference.get());
        obj = null;
        System.gc();
        System.out.println(obj);
        System.out.println(softReference.get());
    }

    // -Xms5m -Xmx5m -XX:+PrintGCDetails
    public static void softRef_Memory_NotEnough() {
        Object obj = new Object();
        SoftReference<Object> softReference = new SoftReference<>(obj);
        System.out.println(obj);
        System.out.println(softReference.get());
        obj = null;

        try {
            byte[] arr = new byte[30 * 1024 * 1024];
        } catch (Exception e) {

        } finally {
            System.out.println(obj);
            System.out.println(softReference.get());
        }
    }
    public static void main(String[] args) {
//        softRef_Memory_Enough();
        softRef_Memory_NotEnough();
    }
}
