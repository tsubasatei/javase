package com.xt.juc;

/**
 * 模拟 CAS 算法
 */
public class CASTest {
    public static void main(String[] args) {
        MyCAS cas = new MyCAS();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                int expected = cas.get();
                boolean flag = cas.compareAndSet(expected, (int) (Math.random() * 101));
                System.out.println(flag);
            }).start();
        }
    }
}

class MyCAS {
    private int value;

    // 获取内存值
    public synchronized int get() {
        return value;
    }

    // 比较
    public synchronized int compareAndSwap (int expected, int newValue) {
        int old = value;
        if (old == expected) {
            this.value = newValue;
        }
        return old;
    }

    // 设置
    public synchronized boolean compareAndSet (int expected, int newValue) {
        return expected == compareAndSwap(expected, newValue);
    }
}
