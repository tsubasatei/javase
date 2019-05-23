package com.xt.mytest.singleton;

/**
 * 第三种单例
 */
public class Singleton3 {
    private Singleton3() {
    }
    public static Singleton3 instance;
    static {
        instance = new Singleton3();
    }

}
