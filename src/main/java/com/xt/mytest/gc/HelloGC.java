package com.xt.mytest.gc;

/**
 * @author xt
 * @create 2019/4/24 7:41
 * @Desc
 */
public class HelloGC {
    public static void main(String[] args) {
        System.out.println("CPU核数\t" + Runtime.getRuntime().availableProcessors());
        System.out.println(Runtime.getRuntime().totalMemory());
        System.out.println(Runtime.getRuntime().maxMemory());
    }
}
