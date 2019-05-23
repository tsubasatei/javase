package com.xt.mytest.singleton;

/**
 * 第四种单例
 */
public class Singleton4 {
    private static volatile Singleton4 instance;
    private Singleton4() {

    }
    public static Singleton4 getInstance() {
        if (instance == null) {
            synchronized (Singleton4.class) {
                if (instance == null) {
                    instance = new Singleton4();
                }
            }
        }
        return instance;
    }
}
