/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.database.connection;

import br.jpe.core.database.DBException;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * An interface for Connections
 *
 * @author joaovperin
 */
public interface Connection extends AutoCloseable {

    /**
     * Creates a new SQL Statement
     *
     * @return Statement
     * @throws DBException
     */
    public Statement createStmt() throws DBException;

    /**
     * Prepares a new SQL Statement
     *
     * @param sql
     * @return PreparedStatement
     * @throws DBException
     */
    public PreparedStatement prepareStmt(String sql) throws DBException;

    /**
     * Commit the connection
     *
     * @throws DBException
     */
    public void commit() throws DBException;

    /**
     * Rollbacks the connection
     *
     * @throws DBException
     */
    public void rollback() throws DBException;

    /**
     * Closes the connection
     *
     * @throws DBException
     */
    @Override
    public void close() throws DBException;

}
