package com.xt.mytest.juc;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 线程不安全
 * java.util.ConcurrentModificationException
 */
public class ArrayListNotSafeTest {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList();

        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
//                list.add(UUID.randomUUID().toString().substring(0, 8));
                copyOnWriteArrayList.add(UUID.randomUUID().toString().substring(0, 8));
//                System.out.println(list);
                System.out.println(copyOnWriteArrayList);
            }, String.valueOf(i)).start();
        }
    }
}
