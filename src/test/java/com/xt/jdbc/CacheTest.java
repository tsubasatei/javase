package com.xt.jdbc;

/**
 * @author xt
 * @create 2019/5/19 8:19
 * @Desc
 */
public class CacheTest {

    public static void main(String[] args) {
        //情景1
        Integer a = 1;
        Integer b = 1;
        System.out.println(a == b);//true。b.intValue()

        //情景2
        Integer c = 128;
        Integer d = 128;
        System.out.println(c == d);//false

        //情景3
        Integer e = new Integer(1);
        Integer f = new Integer(1);
        System.out.println(e == f);//false

        System.out.println("-------------");

        int x = 1;
        Integer y = Integer.valueOf(1);
        Integer z = new Integer(1);

        System.out.println(x == y);//true 自动拆箱
        System.out.println(x == z);//true
        System.out.println(y == z); // false
    }
}
