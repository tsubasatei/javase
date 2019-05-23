package com.xt.exec.gc;

import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * WeakHashMap
 */
public class WeakHashMapDemo {

    public static void main(String[] args) {
        myHashMap();
        System.out.println("==============");
        myWeakHashMap();

    }

    private static void myWeakHashMap() {
        WeakHashMap<Integer, String> map = new WeakHashMap<>();
        Integer key = new Integer(1);
        String value  = "HashMap";

        map.put(key, value);
        System.out.println(map);
        key = null;
        System.gc();
        System.out.println(map);
        System.out.println(map.size());
    }

    private static void myHashMap() {
        HashMap<Integer, String> map = new HashMap<>();
        Integer key = new Integer(1);
        String value  = "HashMap";

        map.put(key, value);
        System.out.println(map);
        key = null;
        System.gc();
        System.out.println(map);
        System.out.println(map.size());
    }
}
