/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.database;

import br.jpe.core.utils.CryptUtils;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import java.sql.Connection;
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
public final class ConexaoFactory {

    /** Url to the database */
    private static final String URL = "jdbc:mysql://porto-zoca.cl6eed1myiqo.us-west-2.rds.amazonaws.com:3306/PortoZoca_Dev";
    /** Connection properties */
    private static final Properties CONN_PTS;

    static {
        // Static constructor to load properties and register driver
        CONN_PTS = new Properties();
        loadProperties();
        registerDriver();
        // Add's a shutdown hook to deregister the driver
        Runtime.getRuntime().addShutdownHook(new Thread(ConexaoFactory::onFinish));
    }

    /**
     * Prevents instantiation
     */
    private ConexaoFactory() {
        throw new UnsupportedOperationException("Don't Instantiate that!");
    }

    /**
     * Returns a ReadOnly Connection
     *
     * @return Conexao
     * @throws br.portozoca.ws.database.DBException
     */
    public static Conexao query() throws DBException {
        ConexaoPool conn = null;
        ConnectionPool pool = ConnectionPool.get();
        try {
            conn = pool.getConnection();
        } catch (EmptyPoolException ex) {
            try {
                Connection jdbcConn = getJdbcConn();
                jdbcConn.setAutoCommit(false);
                jdbcConn.setReadOnly(true);
                conn = new MySQLConnection(jdbcConn);
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
     * @return Conexao
     * @throws br.portozoca.ws.database.DBException
     */
    public static Conexao transaction() throws DBException {
        Connection conn = null;
        try {
            conn = getJdbcConn();
            conn.setAutoCommit(false);
            conn.setReadOnly(false);
        } catch (SQLException e) {
            throw new DBException("Failed to create a Transaction Connection", e);
        }
        return new MySQLConnection(conn, false);
    }

    /**
     * Returns a JdbcConnection
     *
     * @return Connection
     * @throws SQLException
     */
    private static Connection getJdbcConn() throws SQLException {
        return DriverManager.getConnection(URL, CONN_PTS);
    }

    /**
     * Loads the properties to use on connections
     *
     * Reference:
     * https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-configuration-properties.html
     */
    private static void loadProperties() {
        CONN_PTS.clear();
        CONN_PTS.setProperty("user", "PortoZoca");
        CONN_PTS.setProperty("password", CryptUtils.base64().decrypt("UG9ydG9ab2NhMTIzNA=="));
    }

    /**
     * Register the MySQL/Jdbc Driver
     */
    private static void registerDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
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
                if (d.acceptsURL(URL)) {
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
