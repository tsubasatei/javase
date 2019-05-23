package com.xt.mytest.singleton;

/**
 * 第一种单利模式
 */
public class Singleton1 {
    public static final Singleton1 INSTANCE = new Singleton1();
    private Singleton1(){

    }
}
