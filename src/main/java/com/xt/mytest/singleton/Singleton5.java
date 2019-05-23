package com.xt.mytest.singleton;

/**
 * @author xt
 * @create 2019/4/23 14:52
 * @Desc
 */
public class Singleton5 {
    private Singleton5() {

    }
    private static class Inner {
        private static final Singleton5 INSTANCE = new Singleton5();
    }
    public static Singleton5 getInstance() {
        return Inner.INSTANCE;
    }
}
