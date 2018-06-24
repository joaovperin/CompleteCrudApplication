/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.database.sql.connection;

import br.jpe.core.database.Connection;
import br.jpe.core.database.DBException;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * SQL Connection interface
 *
 * @author joaovperin
 */
public interface SQLConnection extends Connection {

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

}
