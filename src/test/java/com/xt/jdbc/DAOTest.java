package com.xt.jdbc;

import org.junit.Test;

import java.util.List;

/**
 * DAO 测试类
 */
public class DAOTest {

    DAO dao = new DAO();

    @Test
    public void update() {
        String sql = "UPDATE customers SET NAME='Jerry' WHERE id = ?";
        dao.update(sql, 1);
    }

    @Test
    public void get() {
        String sql = "SELECT id, name, email, birth FROM customers WHERE id = ?";
        Customer customer = dao.get(Customer.class, sql, 1);
        System.out.println(customer);
    }

    @Test
    public void getForList() {
        String sql = "SELECT id, name, email, birth FROM customers ";
        List<Customer> customers = dao.getForList(Customer.class, sql);
        System.out.println(customers);
    }

    @Test
    public void getForObject () {
        String sql = "SELECT name FROM customers where id = ?";
        String name = dao.getForObject(sql, 1);
        System.out.println(name);
    }
}