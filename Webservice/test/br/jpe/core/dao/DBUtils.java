/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import br.jpe.core.database.DBException;
import br.jpe.core.database.connection.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Prepares the database for tests
 *
 * @author joaovperin
 */
public class DBUtils {

    /**
     * Creates the database if not exists
     *
     * @param conn
     * @throws DBException
     */
    public static final void createTestDB(Connection conn) throws DBException {
        // Create table
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS TestBean (");
        sb.append("Keyy int not null auto_increment, ");
        sb.append("Valuee varchar(255), ");
        sb.append("PRIMARY KEY (Keyy))");
        try (Statement stm = conn.createStmt()) {
            stm.executeUpdate(sb.toString());
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Drops the database if exists
     *
     * @param conn
     * @throws DBException
     */
    public static final void deleteTestDB(Connection conn) throws DBException {
        try (Statement stm = conn.createStmt()) {
            stm.executeUpdate("DROP TABLE IF EXISTS TestBean");
        } catch (SQLException e) {
            throw new DBException(e);
        }

    }

}
