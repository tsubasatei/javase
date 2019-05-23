package com.xt.test;

/**
 * @author xt
 * @create 2019/5/15 15:33
 * @Desc
 */
public class StringTest {

    String str = new String( "good" );
    char [] ch = { 't' , 'e' , 's' , 't' };
    public void change(String str , char ch []) {
        str = "test ok" ;
        ch [0] = 'b' ;
    }
    public static void main(String[] args ) {
//        StringTest ex = new StringTest();
//        ex .change(ex .str , ex .ch );
//        System.out.print( ex .str + " and" ); //  good andbest
//        System.out .println( ex .ch );

        String str = null;
        StringBuffer sb = new StringBuffer();
        sb.append(str);

        System.out.println(sb.length()); // 4

        System.out.println(sb); // null

        StringBuffer sb1 = new StringBuffer(str);
        System.out.println(sb1);  // java.lang.NullPointerException

    }
}
