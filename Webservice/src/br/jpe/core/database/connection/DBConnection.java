/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.database.connection;

import br.jpe.core.database.DBException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * An implementation of a Poolable Database Connection
 *
 * @author joaovperin
 */
public class DBConnection implements PoolConnection {

    /** JDBC wrapped connection object */
    private final java.sql.Connection jdbcConn;
    /** An indicator if the connection is a transaction or a read-only one */
    private final boolean readOnly;
    /** A flag to control if the connection is free to use */
    private boolean free;
    /** A flag to control if the connection is already commited */
    private boolean commited;

    /**
     * Constructor that receives the connection and default's to readonly
     *
     * @param conn
     */
    public DBConnection(Connection conn) {
        this(conn, true);
    }

    /**
     * Constructor that receives the connection and read-only/transaction mode
     *
     * @param conn
     * @param readOnly
     */
    public DBConnection(java.sql.Connection conn, boolean readOnly) {
        this.jdbcConn = conn;
        this.readOnly = readOnly;
        this.free = true;
        this.commited = false;
    }

    /**
     * Creates a SQL Statement
     *
     * @return Statement
     * @throws DBException
     */
    @Override
    public Statement createStmt() throws DBException {
        try {
            busy();
            return jdbcConn.createStatement();
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    /**
     * Prepares a SQL Statement
     *
     * @return PreparedStatement
     * @throws DBException
     */
    @Override
    public PreparedStatement prepareStmt(String sql) throws DBException {
        try {
            busy();
            return jdbcConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    /**
     * Indicates a connection is busy and cannot be used
     */
    @Override
    public void busy() {
        this.free = false;
    }

    /**
     * Frees the connection
     */
    @Override
    public void free() {
        this.free = true;
    }

    /**
     * Returns true if the connection is Free
     *
     * @return boolean
     */
    @Override
    public boolean isFree() {
        return free;
    }

    /**
     * Commits the transaction
     *
     * @throws DBException
     */
    @Override
    public void commit() throws DBException {
        if (readOnly) {
            System.out.println("*** Cannot Commit on Read-Only connection.");
            return;
        }
        try {
            jdbcConn.commit();
            commited = true;
        } catch (SQLException e) {
            throw new DBException("Failed to commit!", e);
        }
    }

    /**
     * Rollbacks the transaction
     */
    @Override
    public void rollback() {
        if (readOnly) {
            System.out.println("*** Cannot rollback on Read-Only connection.");
            return;
        }
        try {
            jdbcConn.rollback();
        } catch (SQLException ex) {
            System.out.println("*** Failed to rollback.");
        }
    }

    /**
     * Closes the connection
     */
    @Override
    public void close() {
        // If is a ready-only connection, just free that (without closing).
        if (readOnly) {
            free();
            return;
        }
        // If it's a transaction connection and it hasn't been commited yet,
        //...call rollback, so we can use try-with-resources.
        if (!commited) {
            rollback();
        }
        jdbcClose();
    }

    /**
     * Closes the jdbc connection
     */
    @Override
    public final void jdbcClose() {
        try {
            jdbcConn.close();
        } catch (SQLException ex) {
            System.out.println("*** Failed to close.");
        }
    }

}
