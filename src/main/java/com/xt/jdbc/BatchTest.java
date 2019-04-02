package com.xt.jdbc;

import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;

/**
 * 批量操作
 */
public class BatchTest {

    @Test
    public void testBatch(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JDBCTools.getConnection();
            JDBCTools.beginTx(connection);

            String sql = "INSERT INTO customers(id, NAME, BIRTH) VALUES (?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            Date date = new Date(new java.util.Date().getTime());

            Instant start = Instant.now();
            for (int i = 0; i < 1000000; i++) {
                preparedStatement.setInt(1, i + 1);
                preparedStatement.setString(2, "name" + (i+1));
                preparedStatement.setDate(3, date);

                //"积攒" SQL
                preparedStatement.addBatch();

                //当 "积攒" 到一定程度, 就统一的执行一次. 并且清空先前 "积攒" 的 SQL
                if (i % 300 == 0) {
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                }
            }

            //若总条数不是批量数值的整数倍, 则还需要再额外的执行一次.
            if (1000000 % 300 != 0) {
                preparedStatement.executeBatch();
                preparedStatement.clearBatch();
            }

            Instant end = Instant.now();
            System.out.println("消耗时间：" + Duration.between(start, end));

            JDBCTools.commit(connection);

        } catch (Exception e) {
            e.printStackTrace();
            JDBCTools.rollback(connection);
        } finally {
            JDBCTools.release(null, preparedStatement, connection);
        }
    }

    @Test
    public void testBatchWithPreparedStatement() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JDBCTools.getConnection();
            JDBCTools.beginTx(connection);

            String sql = "INSERT INTO customers(id, NAME, BIRTH) VALUES (?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            Date date = new Date(new java.util.Date().getTime());

            Instant start = Instant.now();
            for (int i = 0; i < 1000000; i++) {
               preparedStatement.setInt(1, i + 1);
               preparedStatement.setString(2, "name" + (i+1));
               preparedStatement.setDate(3, date);

               preparedStatement.executeUpdate();
            }
            Instant end = Instant.now();
            System.out.println("消耗时间：" + Duration.between(start, end));

            JDBCTools.commit(connection);

        } catch (Exception e) {
            e.printStackTrace();
            JDBCTools.rollback(connection);
        } finally {
            JDBCTools.release(null, preparedStatement, connection);
        }
    }

    /**
     * 向  Oracle 的 customers 数据表中插入 10 万条记录
     * 测试如何插入, 用时最短.
     * 1. 使用 Statement.
     */
    @Test
    public void testBatchWithStatement() {
        Connection connection = null;
        Statement statement = null;
        String sql = null;

        try {
            connection = JDBCTools.getConnection();
            JDBCTools.beginTx(connection);
            statement = connection.createStatement();

            Instant start = Instant.now();

            for (int i = 0; i < 1000000; i++) {
                sql = "INSERT INTO customers(id, NAME, BIRTH) VALUES ("
                        + (i+1)
                        +", 'name" + (i + 1)
                        + "', '"
                        + "2-4月 -19"
                        +"')";
                statement.executeUpdate(sql);
            }
            Instant end = Instant.now();
            System.out.println("消耗时间：" + Duration.between(start, end));

            JDBCTools.commit(connection);
        } catch (Exception e) {
            e.printStackTrace();
            JDBCTools.rollback(connection);
        } finally {
            JDBCTools.release(null, statement, connection);
        }
    }
}
