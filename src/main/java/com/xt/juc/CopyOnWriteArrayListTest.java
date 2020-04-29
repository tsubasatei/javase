package com.xt.juc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWriteArrayList / CopyOnWriteArraySet : 写入并复制
 * 注意：
 *  添加操作多时，效率低，因为每次添加是都会进行复制，开销非常大。
 *  并发迭代操作多时可以选择。
 */
public class CopyOnWriteArrayListTest {

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Thread(new HelloThread()).start();
        }
    }
}

class HelloThread implements Runnable {

    // java.util.ConcurrentModificationException
//    private static List<String> list = Collections.synchronizedList(new ArrayList<>());
    private static CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

    static {
        list.add("AA");
        list.add("BB");
        list.add("CC");
    }

    @Override
    public void run() {
        Iterator<String> iter = list.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
            list.add("DD");
        }
    }
}