/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.database.sql;

import br.jpe.core.database.DBException;
import br.jpe.core.database.Connection;
import br.jpe.core.database.sql.connection.DBConnection;
import br.jpe.core.database.sql.connection.PoolConnection;
import br.jpe.core.database.sql.connection.SQLConnection;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import java.io.IOException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * A factory for database connections
 *
 * @author joaovperin
 */
public final class ConnectionFactory {

    /** Connection properties */
    private static final Properties CONN_PTS;
    /** Database Connection */
    private static final Properties APP_PTS;

    static {
        // Static constructor to load properties and register driver
        APP_PTS = new Properties();
        CONN_PTS = new Properties();
        loadProperties();
        registerDriver();
        // Add's a shutdown hook to deregister the driver
        Runtime.getRuntime().addShutdownHook(new Thread(ConnectionFactory::onFinish));
    }

    /**
     * Prevents instantiation
     */
    private ConnectionFactory() {
        throw new UnsupportedOperationException("Don't Instantiate that!");
    }

    /**
     * Returns a ReadOnly Connection
     *
     * @return Connection
     * @throws br.jpe.core.database.sql.DBException
     */
    public static SQLConnection query() throws DBException {
        PoolConnection conn = null;
        ConnectionPool pool = ConnectionPool.get();
        try {
            conn = pool.getConnection();
        } catch (EmptyPoolException ex) {
            try {
                java.sql.Connection jdbcConn = getJdbcConn();
                jdbcConn.setAutoCommit(false);
                jdbcConn.setReadOnly(true);
                conn = new DBConnection(jdbcConn);
                pool.addConnection(conn);
            } catch (SQLException e) {
                throw new DBException("Failed to create a Read-Only Connection", e);
            }
        }
        return conn;
    }

    /**
     * Returns a Transaction Connection
     *
     * @return Connection
     * @throws br.jpe.core.database.sql.DBException
     */
    public static SQLConnection transaction() throws DBException {
        java.sql.Connection conn = null;
        try {
            conn = getJdbcConn();
            conn.setAutoCommit(false);
            conn.setReadOnly(false);
        } catch (SQLException e) {
            throw new DBException("Failed to create a Transaction Connection", e);
        }
        return new DBConnection(conn, false);
    }

    /**
     * Returns a JdbcConnection
     *
     * @return java.sql.Connection
     * @throws SQLException
     */
    private static java.sql.Connection getJdbcConn() throws SQLException {
        return DriverManager.getConnection(APP_PTS.getProperty("db.connection"), CONN_PTS);
    }

    /**
     * Loads the properties to use on connections
     *
     * Reference:
     * https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-configuration-properties.html
     */
    private static void loadProperties() {
        try {
            APP_PTS.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Fail to load app properties! Please include it on app.properties file in the Resources folder.", e);
        }
        // Clear the database connection properties and reloads
        CONN_PTS.clear();
        CONN_PTS.setProperty("user", APP_PTS.getProperty("db.username"));
        CONN_PTS.setProperty("password", APP_PTS.getProperty("db.password"));
    }

    /**
     * Register the MySQL/Jdbc Driver
     */
    private static void registerDriver() {
        try {
            Class.forName(APP_PTS.getProperty("db.driver"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to register JDBC/Mysql Driver!", e);
        }
    }

    /**
     * Does all the stuff to finish the application
     */
    private static void onFinish() {
        // Deregister the MySQL/Jdbc Driver
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver d = drivers.nextElement();
            try {
                if (d.acceptsURL(APP_PTS.getProperty("db.connection"))) {
                    DriverManager.deregisterDriver(d);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to deregister JDBC/Mysql Driver!", e);
            }
        }
        // Shutdown the thread to avoid memory leaks
        try {
            AbandonedConnectionCleanupThread.shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to shutdown AbandonedConnectionCleanup Thread", e);
        }
    }

}
