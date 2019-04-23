package com.xt.exec.juc2;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 集合类不安全问题
 * ArrayList
 */
public class ContainerNotSafeDemo {

    public static void main(String[] args) {
        Map<String, String> map = new ConcurrentHashMap<>(); // Collections.synchronizedMap(new HashMap<>());

        for (int i = 1; i <= 20; i++) {
            new Thread(() -> {
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0, 8));
                System.out.println(map);
            }, String.valueOf(i)).start();
        }

    }

    private static void setNotSafe() {
        Set<String> set = new CopyOnWriteArraySet<>(); //Collections.synchronizedSet(new HashSet<>()); //new HashSet<>();

        for (int i = 1; i <= 20; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }
    }

    // java.util.ConcurrentModificationException
    /**
     * 1. 故障现象 java.util.ConcurrentModificationException
     *
     * 2. 导致原因
     *
     * 3. 解决方案
     *  3.1 new Vector<>();
     *  3.2 Collections.synchronizedList(new ArrayList<>());
     *  3.3 new CopyOnWriteArrayList<>();
     */
    private static void listNotSafe() {
        List<String> list = new CopyOnWriteArrayList<>();

        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                list.add((String) UUID.randomUUID().toString().subSequence(0, 8));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }
}
