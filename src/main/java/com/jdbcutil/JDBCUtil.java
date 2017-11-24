package com.jdbcutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;

/**
 * @author xhd
 * @date 2017-10-21
 */

public class JDBCUtil {
    //private static final Boolean AUTO_COMMIT = Boolean.valueOf(true);
    //private static final Integer MINIMUM_IDLE = Integer.valueOf(10);
    //private static final Integer CONNECTION_TIMEOUT = Integer.valueOf(30000);
    //private static final Integer IDLE_TIMEOUT = Integer.valueOf(600000);
    //private static final Integer MAX_LIFE_TIME = Integer.valueOf(1800000);
    //private static final Integer MAXIMUM_POOL_SIZE = Integer.valueOf(500);
    //private static HashMap<String, HikariDataSource> dbPool = new HashMap();
    //private static final Logger log = LoggerFactory.getLogger(JDBCUtil.class);
    //
    //public JDBCUtil() {
    //}
    //
    //public static Connection getConnection(String type, String url, String username, String password) {
    //    DBType dbType = getDBTypeInstance(type);
    //    Connection connection = getConnectionIn(dbType.getDriverClassName(), dbType.getJdbcUrl(url), username, password);
    //    if(null == connection) {
    //        removeConnection(dbType.getDriverClassName(), dbType.getJdbcUrl(url), username);
    //        connection = getConnectionIn(dbType.getDriverClassName(), dbType.getJdbcUrl(url), username, password);
    //    }
    //
    //    return connection;
    //}
    //
    //public static DBType getDBTypeInstance(String type) {
    //    Object dbType;
    //    if("oracle".equals(type)) {
    //        dbType = Oracle.getInstance();
    //    } else if("mysql".equals(type)) {
    //        dbType = Mysql.getInstance();
    //    } else {
    //        dbType = Oracle.getInstance();
    //    }
    //
    //    return (DBType)dbType;
    //}
    //
    //public static void closeAll(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
    //    if(resultSet != null) {
    //        try {
    //            resultSet.close();
    //        } catch (SQLException var6) {
    //            var6.printStackTrace();
    //        }
    //    }
    //
    //    if(preparedStatement != null) {
    //        try {
    //            preparedStatement.close();
    //        } catch (SQLException var5) {
    //            var5.printStackTrace();
    //        }
    //    }
    //
    //    if(connection != null) {
    //        try {
    //            connection.close();
    //        } catch (SQLException var4) {
    //            var4.printStackTrace();
    //        }
    //    }
    //
    //}
    //
    //public static void closeAll(Connection connection, Statement statement, ResultSet resultSet) {
    //    if(resultSet != null) {
    //        try {
    //            resultSet.close();
    //        } catch (SQLException var6) {
    //            var6.printStackTrace();
    //        }
    //    }
    //
    //    if(statement != null) {
    //        try {
    //            statement.close();
    //        } catch (SQLException var5) {
    //            var5.printStackTrace();
    //        }
    //    }
    //
    //    if(connection != null) {
    //        try {
    //            connection.close();
    //        } catch (SQLException var4) {
    //            var4.printStackTrace();
    //        }
    //    }
    //
    //}
    //
    //private static Connection getConnectionIn(String driverClassName, String jdbcUrl, String username, String password) {
    //    String key = driverClassName + jdbcUrl + username;
    //    HikariDataSource datasource;
    //    if(dbPool.containsKey(key)) {
    //        datasource = (HikariDataSource)dbPool.get(key);
    //    } else {
    //        HikariConfig config = new HikariConfig();
    //        config.setDriverClassName(driverClassName);
    //        config.setJdbcUrl(jdbcUrl);
    //        config.setUsername(username);
    //        config.setPassword(password);
    //        config.setAutoCommit(AUTO_COMMIT.booleanValue());
    //        config.setConnectionTimeout((long)CONNECTION_TIMEOUT.intValue());
    //        config.setIdleTimeout((long)IDLE_TIMEOUT.intValue());
    //        config.setMaxLifetime((long)MAX_LIFE_TIME.intValue());
    //        config.setMaximumPoolSize(MAXIMUM_POOL_SIZE.intValue());
    //        config.setMinimumIdle(MINIMUM_IDLE.intValue());
    //        datasource = new HikariDataSource(config);
    //        dbPool.put(key, datasource);
    //    }
    //
    //    try {
    //        return datasource.getConnection();
    //    } catch (SQLException var7) {
    //        log.error("getConnection is error:" + var7.toString());
    //        var7.printStackTrace();
    //        datasource.resumePool();
    //        return null;
    //    }
    //}
    //
    //private static void removeConnection(String driverClassName, String jdbcUrl, String username) {
    //    String key = driverClassName + jdbcUrl + username;
    //    dbPool.remove(key);
    //}
}