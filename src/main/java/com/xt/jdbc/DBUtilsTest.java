package com.xt.jdbc;

import org.apache.commons.dbutils.QueryLoader;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 测试 DBUtils 工具类
 */
public class DBUtilsTest {

    //1. 创建 QueryRunner 的实现类  线程安全的
    QueryRunner queryRunner = new QueryRunner();

    /**
     * QueryLoader: 可以用来加载存放着 SQL 语句的资源文件.
     * 使用该类可以把 SQL 语句外置化到一个资源文件中. 以提供更好的解耦
     * @throws IOException
     */
    @Test
    public void testQueryLoader() throws IOException {
        // / 代表类路径的根目录.
        Map<String, String> sqls = QueryLoader.instance().load("/sql.properties");

        String updateSql = sqls.get("UPDATE_CUSTOMER");
        System.out.println(updateSql);
    }


    /**
     * 1. ResultSetHandler 的作用: QueryRunner 的 query 方法的返回值最终取决于
     * query 方法的 ResultHandler 参数的 hanlde 方法的返回值.
     *
     * 2. BeanListHandler: 把结果集转为一个 Bean 的 List, 并返回. Bean 的类型在
     * 创建 BeanListHanlder 对象时以 Class 对象的方式传入. 可以适应列的别名来映射
     * JavaBean 的属性名:
     * String sql = "SELECT id, name customerName, email, birth " +
     *			"FROM customers WHERE id = ?";
     *
     * BeanListHandler(Class<T> type)
     *
     * 3. BeanHandler: 把结果集转为一个 Bean, 并返回. Bean 的类型在创建 BeanHandler
     * 对象时以 Class 对象的方式传入
     * BeanHandler(Class<T> type)
     *
     * 4. MapHandler: 把结果集转为一个 Map 对象, 并返回. 若结果集中有多条记录, 仅返回
     * 第一条记录对应的 Map 对象. Map 的键: 列名(而非列的别名), 值: 列的值
     *
     * 5. MapListHandler: 把结果集转为一个 Map 对象的集合, 并返回.
     * Map 的键: 列名(而非列的别名), 值: 列的值
     *
     * 6. ScalarHandler: 可以返回指定列的一个值或返回一个统计函数的值.
     */

    @Test
    public void testScalarHandler () {
        Connection connection = null;

        try {
            connection = JDBCTools.getConnection2();
            String sql = "SELECT name FROM customers WHERE id = ?";
            Object obj = queryRunner.query(connection, sql, new ScalarHandler<>(), 1);
            System.out.println(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(null, null, connection);
        }
    }

    @Test
    public void testMapListHandler () {
        Connection connection = null;

        try {
            connection = JDBCTools.getConnection2();
            String sql = "SELECT id, name, email, birth FROM customers";
            List<Map<String, Object>> result = queryRunner.query(connection, sql, new MapListHandler());
            System.out.println(result);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(null, null, connection);
        }
    }

    @Test
    public void testMapHandler () {
        Connection connection = null;

        try {
            connection = JDBCTools.getConnection2();
            String sql = "SELECT id, name, email, birth FROM customers WHERE id = ?";
            Map<String, Object> result = queryRunner.query(connection, sql, new MapHandler(), 1);
            System.out.println(result);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(null, null, connection);
        }
    }

    /**
     * 测试 ResultSetHandler 的 BeanListHandler 实现类
     * BeanListHandler: 把结果集转为一个 Bean 的 List. 该 Bean
     * 的类型在创建 BeanListHandler 对象时传入:
     *
     * new BeanListHandler<>(Customer.class)
     *
     */
    @Test
    public void testBeanListHandler () {
        Connection connection = null;

        try {
            connection = JDBCTools.getConnection2();
            String sql = "SELECT id, name, email, birth FROM customers";
            List<Customer> customers = queryRunner.query(connection, sql, new BeanListHandler<>(Customer.class));
            System.out.println(customers);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(null, null, connection);
        }
    }

    @Test
    public void testBeanHandler () {
        Connection connection = null;

        try {
            connection = JDBCTools.getConnection2();
            String sql = "SELECT id, name, email, birth FROM customers WHERE id >= ?";
            Customer customer = (Customer) queryRunner.query(connection, sql, new BeanHandler(Customer.class), 1);
            System.out.println(customer);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(null, null, connection);
        }
    }

    /**
     * 测试 QueryRunner 的 query 方法
     */
    @Test
    public void testQuery () {
        Connection connection = null;

        try {
            connection = JDBCTools.getConnection2();
            String sql = "SELECT id, name, email, birth FROM customers";
            /**
             * 2. 调用 query 方法:
             * ResultSetHandler 参数的作用: query 方法的返回值直接取决于
             * ResultSetHandler 的 hanlde(ResultSet rs) 是如何实现的. 实际上, 在
             * QueryRunner 类的 query 方法中也是调用了 ResultSetHandler 的 handle()
             * 方法作为返回值的。
             */
            Object object = queryRunner.query(connection, sql,
                    new ResultSetHandler(){
                        @Override
                        public Object handle(ResultSet rs) throws SQLException {
                            List<Customer> customers = new ArrayList<>();

                            while(rs.next()){
                                int id = rs.getInt(1);
                                String name = rs.getString(2);
                                String email = rs.getString(3);
                                Date birth = rs.getDate(4);

                                Customer customer =
                                        new Customer(id, name, email, birth);
                                customers.add(customer);
                            }

                            return customers;
                        }
                    }

            );

            System.out.println(object);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(null, null, connection);
        }
    }
    /**
     * 测试 QueryRunner 类的 update 方法
     * 该方法可用于 INSERT, UPDATE 和 DELETE
     */
    @Test
    public void testUpdate () {


        Connection connection = null;

        try {
            connection = JDBCTools.getConnection2();
            String sql = "DELETE FROM customers WHERE id IN (?, ?)";
            //2. 使用其 update 方法
            queryRunner.update(connection, sql, 6, 7);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(null, null, connection);
        }
    }
}
