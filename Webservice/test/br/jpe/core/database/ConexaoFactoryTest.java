/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests Connection classes
 *
 * @author joaovperin
 */
public class ConexaoFactoryTest {

    /**
     * Tests getConnection methods
     *
     * @throws DBException
     * @throws SQLException
     */
    @Test
    public void getConnection() throws DBException, SQLException {
        ResultSet rs = ConnectionFactory.query().createStmt().executeQuery("SELECT '1' FROM DUAL;");
        String res = null;
        if (rs.next()) {
            res = rs.getString(1);
            assertEquals("1", res);
        } else {
            throw new AssertionError("Expected database to return 1 but returned " + res);
        }
    }

    /**
     * Ensure that database has the tables
     *
     * @throws DBException
     * @throws SQLException
     */
    @Test
    public void showTables() throws Exception {
        ResultSet rs = ConnectionFactory.query().createStmt().executeQuery("Show Tables;");
        List<String> tables = new ArrayList<>();
        while (rs.next()) {
            tables.add(rs.getString(1));
        }
        // Asserts
        assertTrue(tables.contains("Localizacao"));
        assertTrue(tables.contains("Lpn"));
        assertTrue(tables.contains("Produto"));
        assertTrue(tables.contains("Usuario"));
    }

    /**
     * Ensures that the pool is reaprovecting the connections
     *
     * @throws DBException
     * @throws SQLException
     */
    @Test
    public void poolReprovectionTest() throws DBException, SQLException {
        PoolConnection c1 = (PoolConnection) ConnectionFactory.query();
        c1.createStmt();
        c1.free();

        Connection c2 = ConnectionFactory.query();
        c2.createStmt();
        // The same reference
        assertTrue(c1.equals(c2));
    }

    /**
     * Ensures that the pool is creating new connections
     *
     * @throws DBException
     * @throws SQLException
     */
    @Test
    public void poolCreationTest() throws DBException, SQLException {
        Connection c1 = ConnectionFactory.query();
        c1.createStmt();

        PoolConnection c2 = (PoolConnection) ConnectionFactory.query();
        c2.createStmt();
        c2.free();

        PoolConnection c3 = (PoolConnection) ConnectionFactory.query();
        c3.createStmt();
        c3.free();
        // Ensures the pool not return an used connection
        assertFalse(c1.equals(c2));
        assertTrue(c2.equals(c3));
    }

    /**
     * Ensures that the pool is not closing, just freeing the connections
     *
     * @throws DBException
     * @throws SQLException
     */
    @Test
    public void poolConnectionFreeingTest() throws DBException, SQLException {
        int e1 = 0;
        int e2 = 0;
        int e3 = 0;
        try (Connection c1 = ConnectionFactory.query()) {
            ResultSet rs1 = c1.createStmt().executeQuery("SELECT '1' FROM DUAL;");
            if (rs1.next()) {
                e1 = rs1.getInt(1);
            }
            try (Connection c2 = ConnectionFactory.query()) {
                ResultSet rs2 = c2.createStmt().executeQuery("SELECT '2' FROM DUAL;");
                if (rs2.next()) {
                    e2 = rs2.getInt(1);
                }
            }
        }
        try (Connection c3 = ConnectionFactory.query()) {
            ResultSet rs3 = c3.createStmt().executeQuery("SELECT '3' FROM DUAL;");
            if (rs3.next()) {
                e3 = rs3.getInt(1);
            }
        }
        // Asserts it works and doesn't throws exceptions of working on closed connections.
        assertEquals(e1, 1);
        assertEquals(e2, 2);
        assertEquals(e3, 3);
    }

}
