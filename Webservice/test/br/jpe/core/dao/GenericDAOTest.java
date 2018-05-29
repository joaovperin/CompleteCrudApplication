/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import br.jpe.core.database.Connection;
import br.jpe.core.database.ConnectionFactory;
import br.jpe.core.database.DBException;
import java.sql.SQLException;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author programacao
 */
public class GenericDAOTest {

    /**
     * Setup database before executing the tests
     *
     * @throws DBException
     * @throws SQLException
     */
    @BeforeClass
    public static void setUpDatabase() throws DBException, SQLException {
        try (Connection cn = ConnectionFactory.transaction()) {
            // Create table
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE IF NOT EXISTS Test (");
            sb.append("Chave int not null auto_increment, ");
            sb.append("Valor varchar(255), ");
            sb.append("PRIMARY KEY (Chave))");
            cn.createStmt().executeUpdate(sb.toString());
            cn.commit();
        }
    }

    /**
     * Cleans up database after the tests
     *
     * @throws DBException
     * @throws SQLException
     */
    @AfterClass
    public static void cleanUpDatabase() throws DBException, SQLException {
        try (Connection cn = ConnectionFactory.transaction()) {
            cn.createStmt().executeUpdate("DROP TABLE IF EXISTS Test");
            cn.commit();
        }
    }

    /**
     * Tests the select method
     *
     * @throws DBException
     */
    @org.junit.Test
    public void testSelect() throws DBException {
        try (Connection conn = ConnectionFactory.query()) {
            new GenericDAO<>(conn, Test.class).select().forEach((t) -> {
                assertNotNull(t);
            });
        }
    }

    /**
     * Tests the Insert method
     *
     * @throws DBException
     */
    @org.junit.Test
    public void testInsert() throws DBException {
        Test obj = new Test();
        try (Connection cn = ConnectionFactory.transaction()) {
            obj.key = 0;
            obj.value = "useless name here";
            new GenericDAO<>(cn, Test.class).insert(obj);
            cn.commit();
        }
        // Asserts it will get the auto-incremented key
        assertEquals(obj.key, 1);
    }

    /**
     * Just for testing purposes.
     */
    private static class Test {

        public Test() {
        }

        private int key;
        private String value;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Test{" + "chave=" + key + ", valor=" + value + '}';
        }

    }

}
