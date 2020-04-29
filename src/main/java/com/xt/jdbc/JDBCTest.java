package com.xt.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.Test;

import java.io.*;
import java.sql.*;
import java.util.Properties;

/**
 *
 */
public class JDBCTest {
    DAO dao = new DAO();

    /**
     * 如何使用 JDBC 调用存储在数据库中的函数或存储过程
     */
    @Test
    public void testCallableStatment() {
        Connection connection = null;
        CallableStatement callableStatement = null;

        try {
            connection = JDBCTools.getConnection2();

            // 1. 通过 Connection 对象的 prepareCall()
            // 方法创建一个 CallableStatement 对象的实例.
            // 在使用 Connection 对象的 preparedCall() 方法时,
            // 需要传入一个 String 类型的字符串, 该字符串用于指明如何调用存储过程.
            String sql = "{?= call sum_salary(?, ?)}";
            callableStatement = connection.prepareCall(sql);

            // 2. 通过 CallableStatement 对象的 reisterOutParameter() 方法注册 OUT 参数.
            callableStatement.registerOutParameter(1, Types.NUMERIC);
            callableStatement.registerOutParameter(3, Types.NUMERIC);

            // 3. 通过 CallableStatement 对象的 setXxx() 方法设定 IN 或 IN OUT 参数. 若想将参数默认值设为 null, 可以使用 setNull() 方法.
            callableStatement.setInt(2, 80);

            // 4. 通过 CallableStatement 对象的 execute() 方法执行存储过程
            callableStatement.execute();

            // 5. 如果所调用的是带返回参数的存储过程,
            //还需要通过 CallableStatement 对象的 getXxx() 方法获取其返回值.
            double sumSalary = callableStatement.getDouble(1);
            long empCount = callableStatement.getLong(3);

            System.out.println(sumSalary);
            System.out.println(empCount);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(null, callableStatement, connection);
        }
    }

    // 使用C3P0获取连接
    @Test
    public void testGetConnection2 () throws SQLException {
        Connection connection2 = JDBCTools.getConnection2();
        System.out.println(connection2);
    }

    /**
     * 1. 创建 c3p0-config.xml 文件, 参考帮助文档中 Appendix B: Configuation Files 的内容
     * 2. 创建 ComboPooledDataSource 实例；DataSource dataSource = new ComboPooledDataSource("mysqlc3p0");
     * 3. 从 DataSource 实例中获取数据库连接.
     */
    @Test
    public void testC3P0WithConfigFile () throws SQLException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource("mysqlc3p0");
        System.out.println(dataSource.getConnection());
    }

    @Test
    public void testC3P0 () throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setJdbcUrl("jdbc:mysql:///test?useSSL=false");
        dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");

        System.out.println(dataSource.getConnection());
    }
    /**
     * 1. 加载 dbcp 的 properties 配置文件: 配置文件中的键需要来自 BasicDataSource 的属性.
     * 2. 调用 BasicDataSourceFactory 的 createDataSource 方法创建 DataSource 实例
     * 3. 从 DataSource 实例中获取数据库连接.
     */
    @Test
    public void testDBCPWithBasicDataSourceFactory () throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = JDBCTest.class.getClassLoader().getResourceAsStream("dbcp.properties");
        properties.load(inputStream);
        BasicDataSource dataSource = BasicDataSourceFactory.createDataSource(properties);
        System.out.println(dataSource.getConnection());
        System.out.println(dataSource.getMaxWaitMillis());

    }

    /**
     * 使用 DBCP 数据库连接池
     * 1. 加入 jar 包(2 个jar 包). 依赖于 Commons Pool
     * 2. 创建数据库连接池
     * 3. 为数据源实例指定必须的属性
     * 4. 从数据源中获取数据库连接
     * @throws SQLException
     */
    @Test
    public void testDBCP () throws SQLException {
        BasicDataSource dataSource = new BasicDataSource();

        //2. 为数据源实例指定必须的属性
        dataSource.setUrl("jdbc:mysql:///test?useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        //3. 指定数据源的一些可选的属性.
        //1). 指定数据库连接池中初始化连接数的个数
        dataSource.setInitialSize(5);

        //2). 指定最大的连接数: 同一时刻可以同时向数据库申请的连接数
        dataSource.setMaxTotal(5);

        //3). 指定最小连接数: 在数据库连接池中保存的最少的空闲连接的数量
        dataSource.setMinIdle(2);

        //4).等待数据库连接池分配连接的最长时间. 单位为毫秒. 超出该时间将抛出异常.
        dataSource.setMaxWaitMillis(5000);

        //4. 从数据源中获取数据库连接
        Connection connection = dataSource.getConnection();
        System.out.println(connection.getClass().getName());

        connection = dataSource.getConnection();
        System.out.println(connection.getClass().getName());

        connection = dataSource.getConnection();
        System.out.println(connection.getClass().getName());

        connection = dataSource.getConnection();
        System.out.println(connection.getClass().getName());

        Connection connection2 = dataSource.getConnection();
        System.out.println(">" + connection.getClass().getName());

        new Thread(new Runnable() {
            Connection conn;
            @Override
            public void run() {
                try {
                    conn = dataSource.getConnection();
                    System.out.println(conn.getClass().getName());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            Thread.sleep(5500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        connection2.close();
    }
    /**
     * 测试事务的隔离级别 在 JDBC 程序中可以通过 Connection 的 setTransactionIsolation 来设置事务的隔离级别.
     */
    @Test
    public void testTransactionIsolationUpdate() {
        Connection connection = null;

        try {
            connection = JDBCTools.getConnection();
            connection.setAutoCommit(false);

            String sql = "UPDATE users SET balance =  balance - 500 WHERE id = 1";
            dao.update(connection, sql);

            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            JDBCTools.release(null, null, connection);
        }
    }

    @Test
    public void testTransactionIsolationRead() {
        String sql = "SELECT balance FROM users WHERE id = 1";
        int balance = dao.getForObject(sql);
        System.out.println(balance);
    }

    /**
     * Tom 给 Jerry 汇款 500 元.
     *
     * 关于事务: 1. 如果多个操作, 每个操作使用的是自己的单独的连接, 则无法保证事务.
     * 2. 具体步骤:
     * 1). 事务操作开始前, 开始事务:取消 Connection 的默认提交行为. connection.setAutoCommit(false);
     * 2). 如果事务的操作都成功,则提交事务: connection.commit();
     * 3). 回滚事务: 若出现异常, 则在 catch 块中回滚事务:
     */
    @Test
    public void testTransaction () {
        Connection connection = null;
        DAO dao = new DAO();
        try {
            connection = JDBCTools.getConnection();
            System.out.println(connection.getAutoCommit());

            // 开始事务: 取消默认提交.
            connection.setAutoCommit(false);

            String sql = "UPDATE users SET balance = balance - 500 WHERE id = 1";
            dao.update(connection, sql);

            int i = 10 / 0;
            String sql2 = "UPDATE users SET balance = balance + 500 WHERE id = 2";
            dao.update(connection, sql2);

            // 提交事务
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            // 回滚事务
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            JDBCTools.release(null, null, connection);
        }
    }

    /*
     * try {
     *
     * //开始事务: 取消默认提交. connection.setAutoCommit(false);
     *
     * //...
     *
     * //提交事务 connection.commit(); } catch (Exception e) { //...
     *
     * //回滚事务 try { connection.rollback(); } catch (SQLException e1) {
     * e1.printStackTrace(); } } finally{ JDBCTools.releaseDB(null, null,
     * connection); }
     */

    /**
     * 读取 blob 数据:
     * 1. 使用 getBlob 方法读取到 Blob 对象
     * 2. 调用 Blob 的 getBinaryStream() 方法得到输入流。再使用 IO 操作即可.
     */
    @Test
    public void testReadBlob () throws IOException {
//        Connection connection = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet resultSet = null;
//        InputStream inputStream = null;
//        try (OutputStream outputStream = new FileOutputStream("picture2.jpg")){
//            connection = JDBCTools.getConnection();
//            String sql = "SELECT id, name, email, birth, picture FROM customers WHERE id = ?";
//            preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setInt(1, 7);
//            resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                int id = resultSet.getInt(1);
//                String name = resultSet.getString(2);
//                String email = resultSet.getString(3);
//                Date birth = resultSet.getDate(4);
//
//                Customer customer = new Customer(id, name, email, birth);
//                System.out.println(customer.toString());
//
//                inputStream = resultSet.getBinaryStream(5);
//                inputStream.transferTo(outputStream);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            inputStream.close();
//            JDBCTools.release(resultSet, preparedStatement, connection);
//        }
    }
    
    /**
     * 插入 BLOB 类型的数据必须使用 PreparedStatement：因为 BLOB 类型的数据时无法使用字符串拼写的。
     *
     * 调用 setBlob(int index, InputStream inputStream)
     */
    @Test
    public void testInsertBlob () {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JDBCTools.getConnection();
            String sql = "INSERT INTO customers(NAME, EMAIL, BIRTH, PICTURE) VALUES (?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "Bonnie");
            preparedStatement.setString(2, "Bonnie@163.com");
            preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
//            InputStream inputStream = new FileInputStream("D:\\background.jpg");
            InputStream inputStream = new FileInputStream("picture.jpg");
            preparedStatement.setBlob(4, inputStream);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(null, preparedStatement, connection);
        }
    }

    /**
     * 取得数据库自动生成的主键
     */
    @Test
    public void testGetKeyValue () {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBCTools.getConnection();
            String sql = "INSERT INTO customers(NAME, EMAIL, BIRTH) VALUES (?,?,?)";

            //使用重载的 prepareStatement(sql, flag) 来生成 PreparedStatement 对象
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, "Elena");
            preparedStatement.setString(2, "Elana@163.com");
            preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));

            preparedStatement.executeUpdate();

            //通过 getGeneratedKeys() 获取包含了新生成的主键的 ResultSet 对象
            //在 ResultSet 中只有一列 GENERATED_KEY, 用于存放新生成的主键值.
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    System.out.println(metaData.getColumnName(i + 1));  // GENERATED_KEY
                    System.out.println(metaData.getColumnLabel(i + 1)); // GENERATED_KEY
                    System.out.println(resultSet.getObject(i + 1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(resultSet, preparedStatement, connection);
        }
    }

    @Test
    public void testGet() {
        String sql = "SELECT id, name, email, birth FROM customers WHERE id = ?";

        Customer customer = JDBCTools.get(Customer.class, sql, 1);
        System.out.println(customer);
    }

    /**
     * 使用 PreparedStatement 将有效的解决 SQL 注入问题.
     */
    @Test
    public void testSQLInjection2 () {
        String username = "a' OR password = ";
        String password = " OR '1' = '1";

        String sql = "SELECT * FROM users WHERE username = ? AND PASSWORD = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = JDBCTools.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("登录成功！");
            } else {
                System.out.println("用户名与密码不匹配，或用户不存在。");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(rs, ps, conn);
        }
    }

    /**
     * SQL 注入.
     */
    @Test
    public void testSQLInjection () {
//        String username = "Tom";
//        String password = "12345";
        String username = "a' OR password = ";
        String password = " OR '1' = '1";

        String sql = "SELECT * FROM users WHERE username = '"
                + username
                + "' AND PASSWORD = '"
                + password
                + "'";
        System.out.println(sql);
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = JDBCTools.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                System.out.println("登录成功！");
            } else {
                System.out.println("用户名与密码不匹配，或用户不存在。");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(rs, st, conn);
        }
    }

    /**
     * PreparedStatement
     *
     * 可以通过调用 Connection 对象的 preparedStatement() 方法获取 PreparedStatement 对象
     * PreparedStatement 接口是 Statement 的子接口，它表示一条预编译过的 SQL 语句
     * PreparedStatement 对象所代表的 SQL 语句中的参数用问号(?)来表示，调用 PreparedStatement 对象的 setXXX() 方法来设置这些参数.
     *                   setXXX() 方法有两个参数，第一个参数是要设置的 SQL 语句中的参数的索引(从 1 开始)，第二个是设置的 SQL 语句中的参数的值
     *
     * 优点：
     *   代码的可读性和可维护性.
     *   PreparedStatement 能最大可能提高性能
     *   防止 SQL 注入
     *
     */
    @Test
    public void testPreparedStatement () {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCTools.getConnection();
            String sql = "INSERT INTO customers(NAME, EMAIL, BIRTH) VALUES (?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "Jimmy");
            ps.setString(2, "Jimmy@163.com");
            ps.setDate(3, new Date(new java.util.Date().getTime()));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(null, ps, conn);
        }
    }
    /**
     * ResultSet: 结果集. 封装了使用 JDBC 进行查询的结果.
     * 1. 调用 Statement 对象的 executeQuery(sql) 可以得到结果集.
     * 2. ResultSet 返回的实际上就是一张数据表. 有一个指针指向数据表的第一行的前面.
     * 可以调用 next() 方法检测下一行是否有效. 若有效该方法返回 true, 且指针下移. 相当于
     * Iterator 对象的 hasNext() 和 next() 方法的结合体
     * 3. 当指针移位到一行时, 可以通过调用 getXxx(index) 或 getXxx(columnName)
     * 获取每一列的值. 例如: getInt(1), getString("name")
     * 4. ResultSet 当然也需要进行关闭.
     */
    @Test
    public void testResultSet() {
        //获取 id=1 的 customers 数据表的记录, 并打印
        Connection conn = null;
        Statement sm = null;
        ResultSet rs = null;

        try {
            //1. 获取 Connection
            conn = JDBCTools.getConnection();

            //2. 获取 Statement
            sm = conn.createStatement();

            //3. 准备 SQL
            String sql = "SELECT * FROM customers";

            //4. 执行查询, 得到 ResultSet
            rs = sm.executeQuery(sql);

            //5. 处理 ResultSet
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString("name");
                String email = rs.getString(3);
                Date date = rs.getDate("birth");
                System.out.println(id);
                System.out.println(name);
                System.out.println(email);
                System.out.println(date);
                System.out.println("-----------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //6. 关闭数据库资源.
            JDBCTools.release(rs, sm, conn);
        }
    }

    /**
     * 通用的更新的方法: 包括 INSERT、UPDATE、DELETE
     * 版本 1.
     */
    public void update (String sql) {
        Connection conn = null;
        Statement sm = null;

        try {
            conn = JDBCTools.getConnection();
            sm = conn.createStatement();
            sm.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(sm, conn);
        }
    }

    /**
     * 通过 JDBC 向指定的数据表中插入一条记录.
     *
     * 1. Statement: 用于执行 SQL 语句的对象
     * 1). 通过 Connection 的 createStatement() 方法来获取
     * 2). 通过 executeUpdate(sql) 可以执行 SQL 语句.
     * 3). 传入的 SQL 可以是 INSERT, UPDATE 或 DELETE. 但不能是 SELECT
     *
     * 2. Connection、Statement 都是应用程序和数据库服务器的连接资源. 使用后一定要关闭.
     * 需要在 finally 中关闭 Connection 和 Statement 对象.
     *
     * 3. 关闭的顺序是: 先关闭后获取的. 即先关闭 Statement 后关闭 Connection
     */
    @Test
    public void testStatement() {
        //1. 获取数据库连接
        Connection conn = null;
        Statement sm = null;

        try {
            conn = getConnection2();

            //3. 准备插入的 SQL 语句
            String sql = null;
//          sql = "INSERT INTO customers(NAME, EMAIL, BIRTH) VALUES ('Tom', 'tome@163.com', '1983-07-29')";
//          sql = "UPDATE customers SET NAME='Jerry' WHERE id = 2";
            sql = "DELETE FROM customers WHERE id=2";

            //4. 执行插入.
            //1). 获取操作 SQL 语句的 Statement 对象:
            //调用 Connection 的 createStatement() 方法来获取
            sm = conn.createStatement();

            //2). 调用 Statement 对象的 executeUpdate(sql) 执行 SQL 语句进行插入
            sm.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //5. 关闭 Statement 对象.
                if (sm != null) {
                    sm.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //2. 关闭连接
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public Connection getConnection2() throws Exception {
        //1. 准备连接数据库的 4 个字符串.
        //1). 创建 Properties 对象
        Properties pro = new Properties();

        //2). 获取 jdbc.properties 对应的输入流
        InputStream is = getClass().getClassLoader().getResourceAsStream("jdbc.properties");

        //3). 加载 2） 对应的输入流
        pro.load(is);

        //4). 具体决定 user, password 等4 个字符串.
        String driver = pro.getProperty("driver");
        String jdbcUrl = pro.getProperty("jdbcUrl");
        String user = pro.getProperty("user");
        String password = pro.getProperty("password");

        //2. 加载数据库驱动程序(对应的 Driver 实现类中有注册驱动的静态代码块.)
        Class.forName(driver);

        //3. 通过 DriverManager 的 getConnection() 方法获取数据库连接.
        Connection connection = DriverManager.getConnection(jdbcUrl, user, password);

        return connection;
    }

    /**
     * DriverManager 是驱动的管理类.
     * 1). 可以通过重载的 getConnection() 方法获取数据库连接. 较为方便
     * 2). 可以同时管理多个驱动程序: 若注册了多个数据库连接, 则调用 getConnection()
     * 方法时传入的参数不同, 即返回不同的数据库连接。
     *
     * @throws Exception
     */
    @Test
    public void testDriverManger () throws Exception {
        //1. 准备连接数据库的 4 个字符串.
        //驱动的全类名.
        String driver = "com.mysql.jdbc.Driver";
        //JDBC URL
        String jdbcUrl = "jdbc:mysql://localhost:3306/test";
        //user
        String user = "root";
        //password
        String password = "root";

        //2. 加载数据库驱动程序(对应的 Driver 实现类中有注册驱动的静态代码块.)
        Class.forName(driver);

        //3. 通过 DriverManager 的 getConnection() 方法获取数据库连接.
        Connection connection = DriverManager.getConnection(jdbcUrl, user, password);

        System.out.println(connection);
    }

    /**
     * Driver 是一个接口: 数据库厂商必须提供实现的接口. 能从其中获取数据库连接.
     * 可以通过 Driver 的实现类对象获取数据库连接.
     *
     */
    @Test
    public void testDriver () throws Exception {
        //1. 创建一个 Driver 实现类的对象
        Driver driver = new com.mysql.jdbc.Driver();

        //2. 准备连接数据库的基本信息: url, user, password
        String jdbcUrl = "jdbc:mysql:///test";
        Properties info = new Properties();
        info.put("user", "root");
        info.put("password", "root");

        //3. 调用 Driver 接口的　connect(url, info) 获取数据库连接
        Connection conn = driver.connect(jdbcUrl, info);
        System.out.println(conn);

    }

    /**
     * 编写一个通用的方法, 在不修改源程序的情况下, 可以获取任何数据库的连接
     * 解决方案: 把数据库驱动 Driver 实现类的全类名、url、user、password 放入一个
     * 配置文件中, 通过修改配置文件的方式实现和具体的数据库解耦.
     *
     * @throws Exception
     */
    public Connection getConnection () throws Exception {
        String driverClass = null;
        String jdbcUrl = null;
        String user = null;
        String password = null;

        //读取类路径下的 jdbc.properties 文件
        InputStream is = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
        Properties pro = new Properties();
        pro.load(is);
        driverClass = pro.getProperty("driver");
        jdbcUrl = pro.getProperty("jdbcUrl");
        user = pro.getProperty("user");
        password = pro.getProperty("password");

        //通过反射常见 Driver 对象.
        Driver driver = (Driver) Class.forName(driverClass).newInstance();
        Properties info = new Properties();
        info.put("user", user);
        info.put("password", password);

        //通过 Driver 的 connect 方法获取数据库连接.
        Connection connect = driver.connect(jdbcUrl, info);
        return connect;
    }

    @Test
    public void testConnection () throws Exception {
        System.out.println(getConnection());
        System.out.println("------------");
        System.out.println(getConnection2());
    }
}
