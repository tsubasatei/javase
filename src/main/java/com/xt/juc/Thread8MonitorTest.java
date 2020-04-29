package com.xt.juc;

/**
 * 线程八锁
 * 题目：判断打印的 one or two
 *
 * 1. 两个普通同步方法，一个Number对象，两个线程,分别打印，标准打印，打印？ // one two
 * 2. 新增 Thread.sleep()  给 getOne(), 打印？  // one two
 * 3. 新增普通方法 getThree(), 打印？           // three one two
 * 4. 两个普通同步方法，两个 Number 对象， 打印？ // two one
 * 5. 修改 getOne() 为静态同步方法，打印?        // two one
 * 6. 修改两个方法均为静态方法，一个 Number 对象？ // one two
 * 7. 一个静态同步方法，一个非晶态同步方法，两个 Number 对象? // two one
 * 8. 两个静态同步方法，两个 Number 对象？     // one two
 *
 * 线程八锁的关键：
 * 1） 非静态方法的锁默认为 this， 静态方法的锁为 对应的 Class 实例
 * 2） 某一时刻内，只能有一个线程持有锁，无论有几个方法。
 */
public class Thread8MonitorTest {
    public static void main(String[] args) {
        Number number = new Number();
        Number number2 = new Number();
//        new Thread(number::getOne).start();
        new Thread(Number::getOne).start();

//        new Thread(number::getTwo).start();
//        new Thread(number2::getTwo).start();
        new Thread(Number::getTwo).start();

//        new Thread(number::getThree).start();
    }
}

class Number {
    // 2
    public static synchronized void getOne() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one");
    }
    // 1
    public static synchronized void getTwo() {
        System.out.println("two");
    }

    // 3
    public void getThree() {
        System.out.println("three");
    }
}