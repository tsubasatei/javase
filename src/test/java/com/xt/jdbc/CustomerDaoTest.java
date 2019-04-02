package com.xt.jdbc;

import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * @author xt
 * @create 2019/4/2 16:32
 * @Desc
 */
public class CustomerDaoTest extends JdbcDaoImpl<Customer> {

    CustomerDao customerDao = new CustomerDao();

    @Test
    public void batch() {
        Connection connection = null;

        try {
            connection = JDBCTools.getConnection2();
            String sql = "INSERT INTO customers(NAME, EMAIL, BIRTH) VALUES (?, ?, ?), (?, ?, ?)";
            Date date = new Date(new java.util.Date().getTime());
            Object[] obj = {"Bonnie", "Bonnie@163.com", date, "Clause", "Clause@163.com", date};
            customerDao.batch(connection, sql, obj);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(null, null, connection);
        }
    }

    @Test
    public void getForValue() {
        Connection connection = null;

        try {
            connection = JDBCTools.getConnection2();
            String sql = "select name, email, birth from customers where id = ?";
            Object value = customerDao.getForValue(connection, sql, 8);
            System.out.println(value);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(null, null, connection);
        }
    }


    @Test
    public void getForList() {
        Connection connection = null;

        try {
            connection = JDBCTools.getConnection2();
            String sql = "select id, name, email, birth from customers";
            List<Customer> list = customerDao.getForList(connection, sql);
            System.out.println(list);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(null, null, connection);
        }
    }


    @Test
    public void get() {
        Connection connection = null;

        try {
            connection = JDBCTools.getConnection2();
            String sql = "select id, name, email, birth from customers where id = ?";
            Customer customer = (Customer) customerDao.get(connection, sql, 8);
            System.out.println(customer);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(null, null, connection);
        }
    }

    @Test
    public void update(){
        Connection connection = null;

        try {
            connection = JDBCTools.getConnection2();
            String sql = "INSERT INTO customers(NAME, EMAIL, BIRTH) VALUES (?,?,?)";
            customerDao.update(connection, sql, "Damon", "Damon@163.com", new Date(new java.util.Date().getTime()));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(null, null, connection);
        }
    }
}