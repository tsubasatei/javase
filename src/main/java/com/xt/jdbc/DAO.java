package com.xt.jdbc;

import org.apache.commons.beanutils.BeanUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO
 */
public class DAO {

    public void update (Connection connection, String sql, Object ... args) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(null, preparedStatement, null);
        }
    }

    /**
     * INSERT, UPDATE, DELETE 操作都可以包含在其中
     *
     * @param sql
     * @param args
     */
    public void update (String sql, Object ... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JDBCTools.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(null, preparedStatement, connection);
        }
    }

    /**
     * 查询一条记录, 返回对应的对象
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> T get(Class<T> clazz, String sql, Object ... args) {
        List<T> list = getForList(clazz, sql, args);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 传入 SQL 语句和 Class 对象, 返回 SQL 语句查询到的记录对应的 Class 类的对象的集合
     * @param clazz: 对象的类型
     * @param sql: SQL 语句
     * @param args: 填充 SQL 语句的占位符的可变参数.
     * @return
     */
    public <T> List<T> getForList (Class<T> clazz, String sql, Object ... args) {
        List<T> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //1. 得到结果集
            connection = JDBCTools.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();

            //2. 处理结果集, 得到 Map 的 List, 其中一个 Map 对象就是一条记录.
            // Map 的 key 为 reusltSet 中列的别名, Map 的 value 为列的值.
            List<Map<String, Object>> values = handleResultSetToMapList(resultSet);

            //3. 把 Map 的 List 转为 clazz 对应的 List
            //其中 Map 的 key 即为 clazz 对应的对象的 propertyName,
            //而 Map 的 value 即为 clazz 对应的对象的 propertyValue
            list = tranferMapList2BeanList(clazz, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(resultSet, preparedStatement, connection);
        }
        return list;
    }

    /**
     * 返回某条记录的某一个字段的值 或 一个统计的值(一共有多少条记录等.)
     * @param sql
     * @param args
     * @param <E>
     * @return
     */
    public <E> E getForObject(String sql, Object ... args) {
        //1. 得到结果集: 该结果集应该只有一行, 且只有一列
        E obj = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //1. 得到结果集
            connection = JDBCTools.getConnection();
            System.out.println(connection.getTransactionIsolation());
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                obj = (E)resultSet.getObject(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(resultSet, preparedStatement, connection);
        }
        // 返回结果
        return obj;
    }

    private <T> List<T> tranferMapList2BeanList(Class<T> clazz, List<Map<String, Object>> values) throws Exception {
        List<T> list = new ArrayList<>();
        if (values != null && values.size() > 0) {
            for (Map<String, Object> map : values) {
                T entity = clazz.newInstance();;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String propertyName = entry.getKey();
                    Object propertyValue = entry.getValue();
                    BeanUtils.setProperty(entity, propertyName, propertyValue);
                }
                // 13. 把 Object 对象放入到 list 中.
                list.add(entity);
            }
        }
        return list;
    }

    /**
     * 处理结果集, 得到 Map 的一个 List, 其中一个 Map 对象对应一条记录
     *
     * @param resultSet
     * @return
     * @throws Exception
     */
    private List<Map<String, Object>> handleResultSetToMapList(ResultSet resultSet) throws Exception {
        // 5. 准备一个 List<Map<String, Object>>:
        // 键: 存放列的别名, 值: 存放列的值. 其中一个 Map 对象对应着一条记录
        List<Map<String, Object>> values = new ArrayList<>();
        Map<String, Object> map = null;
        while (resultSet.next()) {
            List<String> columnLabels = getColumnLabels(resultSet);
            map = new HashMap<>();
            for (String columnLabel : columnLabels) {
                map.put(columnLabel, resultSet.getObject(columnLabel));
            }
            // 11. 把一条记录的一个 Map 对象放入 5 准备的 List 中
            values.add(map);
        }
        return values;
    }

    /**
     * 获取结果集的 ColumnLabel 对应的 List
     *
     */
    private List<String> getColumnLabels(ResultSet resultSet) throws Exception {
        List<String> list = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            list.add(metaData.getColumnLabel(i + 1));
        }
        return list;
    }
}
