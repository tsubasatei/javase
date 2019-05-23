package com.xt.mytest.singleton;

import java.util.concurrent.*;

/**
 * 单例模式
 */
public class SingletonTest {
    public static void main(String[] args) {

        Singleton5 singleton51 = Singleton5.getInstance();
        Singleton5 singleton52 = Singleton5.getInstance();
        System.out.println(singleton51 == singleton52);

        System.out.println("======第五种方法====");

        Callable<Singleton4> callable  = () -> Singleton4.getInstance();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Singleton4> f1 = executorService.submit(callable);
        Future<Singleton4> f2 = executorService.submit(callable);
        try {
            System.out.println(f1.get() == f2.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

        System.out.println("========第四种=======");

        Singleton3 singleton31 = Singleton3.instance;
        Singleton3 singleton32 = Singleton3.instance;
        System.out.println(singleton31 == singleton32);

        System.out.println("========第三种=======");

        Singleton2 singleton21 = Singleton2.INSTANCE;
        Singleton2 singleton22 = Singleton2.INSTANCE;
        System.out.println(singleton21 == singleton22);

        System.out.println("=============第二种============");

        Singleton1 singleton11 = Singleton1.INSTANCE;
        Singleton1 singleton12 = Singleton1.INSTANCE;
        System.out.println(singleton11 == singleton12);
    }
}
