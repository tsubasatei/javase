package com.xt.array;

import org.junit.Test;

import java.util.Arrays;
import java.util.OptionalInt;

/**
 * @author xt
 * @create 2019/3/29 22:38
 * @Desc
 */
public class ArrayTest {
    @Test
    public void test1 () {
        int[] arr = {2, 3, 5, 7, 11, 13, 17, 19};

        // 数组复制
        int[] copy = Arrays.copyOf(arr, arr.length);
        System.out.println(arr == copy); // false
        Arrays.stream(copy).forEach(System.out::println);
    }

    // 求最大值
    @Test
    public void test () {
        int arr[] = new int[10];
        for(int i = 0; i < arr.length; i++) {
            arr[i] = (int)(Math.random() * (99 - 10 + 1) + 10);
        }

        Arrays.stream(arr).forEach(System.out::println);
        OptionalInt max = Arrays.stream(arr).max();

        System.out.println("数组最大值： " + max.getAsInt());

    }
    @Test
    public void testYangHui () {
        int[][] yanhHui = new int[10][];

        for(int i = 0; i < yanhHui.length; i++) {
            yanhHui[i] = new int[i + 1];
            for(int j = 0; j < yanhHui[i].length; j++) {
                yanhHui[i][0] = yanhHui[i][i] = 1;

                for(int k = 1; k < yanhHui[i].length - 1; k++){
                    yanhHui[i][k] = yanhHui[i-1][k] + yanhHui[i-1][k-1];
                }
            }
        }

        for (int i = 0; i < yanhHui.length; i++) {
            for (int j = 0; j < yanhHui[i].length; j++) {
                System.out.print(yanhHui[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
