package com.xt.jdbc;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xt
 * @create 2019/5/16 8:15
 * @Desc
 */
public class ListTest {

    @Test
    public void testListRemove () {
        List list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);

        updateList(list);
        System.out.println(list);  // [1, 2]

    }

    private static void updateList(List list) {
        list.remove(2);  // 删除的是索引对应的值
    }
}
