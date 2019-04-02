package com.xt.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 操作 JDBC 的工具类. 其中封装了一些工具方法 Version 1
 */
public class JDBCTools {

    private static ComboPooledDataSource dateSource = null;

    static {
        dateSource = new ComboPooledDataSource("mysqlc3p0");
    }

    public static Connection getConnection2() throws SQLException {
        return dateSource.getConnection();
    }
    //处理数据库事务的
    //提交事务
    public static void commit(Connection connection) {
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //回滚事务
    public static void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //开始事务
    public static void beginTx(Connection connection) {
        if (connection != null) {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通用的查询方法：可以根据传入的 SQL、Class 对象返回 SQL 对应的记录的对象
     * @param clazz: 描述对象的类型
     * @param sql: SQL 语句。可能带占位符
     * @param args: 填充占位符的可变参数。
     * @return
     */
    public static <T> T get(Class<T> clazz, String sql, Object ... args) {
        T entity = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //1. 得到 ResultSet 对象
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();

            //2. 得到 ResultSetMetaData 对象
            ResultSetMetaData metaData = resultSet.getMetaData();

            //3. 创建一个 Map<String, Object> 对象, 键: SQL 查询的列的别名, 值: 列的值
            Map<String, Object> values = new HashMap<>();

            //4. 处理结果集. 利用 ResultSetMetaData 填充 3 对应的 Map 对象
            if (resultSet.next()) {
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    Object columnValue = resultSet.getObject(i + 1);

                    values.put(columnLabel, columnValue);
                }
            }

            if (values.size() > 0) {
                entity = clazz.newInstance();

                //5. 遍历 Map 对象, 利用反射为 Class 对象的对应的属性赋值.
                for(Map.Entry<String, Object> entry : values.entrySet()) {
                    String fieldName = entry.getKey();
                    Object value = entry.getValue();

                    ReflectionUtils.setFieldValue(entity, fieldName, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(resultSet, preparedStatement, connection);
        }
        return entity;
    }

    /**
     * 通用   insert、update、delete
     * @param sql
     * @param obj
     */
    public static void update (String sql, Object ... obj) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < obj.length; i++) {
                preparedStatement.setObject(i + 1, obj[i]);
            }
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(null, preparedStatement, connection);
        }
    }

    public static void release(ResultSet resultSet, Statement statement, Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭 Statement 和 Connection
     *
     * @param statement
     * @param connection
     */
    public static void release(Statement statement, Connection connection) {
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 1. 获取连接的方法. 通过读取配置文件从数据库服务器获取一个连接.
     *
     * @return
     */
    public static Connection getConnection() throws Exception {
        // 1. 准备连接数据库的 4 个字符串.
        // 1). 创建 Properties 对象
        Properties properties = new Properties();

        // 2). 获取 jdbc.properties 对应的输入流
        InputStream is = JDBCTools.class.getClassLoader().getResourceAsStream("jdbc.properties");

        // 3). 加载 2） 对应的输入流
        properties.load(is);

        // 4). 具体决定 user, password 等4 个字符串.
        String driver = properties.getProperty("driver");
        String jdbcUrl = properties.getProperty("jdbcUrl");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");

        // 2. 加载数据库驱动程序(对应的 Driver 实现类中有注册驱动的静态代码块.)
        Class.forName(driver);

        // 3. 通过 DriverManager 的 getConnection() 方法获取数据库连接.
        Connection connection = DriverManager.getConnection(jdbcUrl, user, password);

        return connection;

    }
}
