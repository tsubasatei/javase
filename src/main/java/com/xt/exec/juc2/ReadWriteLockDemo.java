package com.xt.exec.juc2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class MyCache{
    private volatile Map<String, Object> map = new HashMap<>();
    private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();

    public void set(String key, Object value) {
        rwlock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t正在写入：" + key);
            // 暂停一会儿线程
            try { TimeUnit.MILLISECONDS.sleep(300); } catch (InterruptedException e) { e.printStackTrace(); }
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + "\t写入完成:" + value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rwlock.writeLock().unlock();
        }

    }

    public void get(String key) {
        rwlock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t正在读取");
            // 暂停一会儿线程
            try { TimeUnit.MILLISECONDS.sleep(300); } catch (InterruptedException e) { e.printStackTrace(); }
            Object result = map.get(key);
            System.out.println(Thread.currentThread().getName() + "\t读取到的值：" + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rwlock.readLock().unlock();
        }
    }

    // 清空缓存
    public void clear() {
        map.clear();
    }
}

/**
 * 读写锁
 */
public class ReadWriteLockDemo {

    public static void main(String[] args) {
        MyCache cache = new MyCache();
        for (int i = 1; i <= 5; i++) {
            final int tmp = i;
            new Thread(() -> {
                cache.set(tmp + "", tmp + "");
            }, String.valueOf(i)).start();
        }

        for (int i = 1; i <= 5; i++) {
            final int tmp = i;
            new Thread(() -> {
                cache.get(tmp + "");
            }, String.valueOf(i)).start();
        }
    }
}
