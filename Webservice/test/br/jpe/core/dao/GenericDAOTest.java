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
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 * GenericDAO Test class
 *
 * @author joaovperin
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
            sb.append("Keyy int not null auto_increment, ");
            sb.append("Valuee varchar(255), ");
            sb.append("PRIMARY KEY (Keyy))");
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
            obj.keyy = 0;
            obj.valuee = "useless name here";
            new GenericDAO<>(cn, Test.class).insert(obj);
            cn.commit();
        }
        // Asserts it will get the auto-incremented key
        assertEquals(1, obj.keyy);
    }

    /**
     * Tests the Update method
     *
     * @throws DBException
     */
    @org.junit.Test
    public void testUpdate() throws DBException {
        try (Connection conn = ConnectionFactory.transaction()) {
            Test obj = new Test();
            obj.keyy = 1;
            obj.valuee = "new Name";
            new GenericDAO<>(conn, Test.class).update(obj);
            conn.commit();
            // Asserts it's changed
            assertEquals(1, obj.keyy);
            assertEquals("new Name", obj.valuee);
            // Asserts it will return an object equal
            Test get = new GenericDAO<Test>(conn, Test.class).select().get(0);
            assertEquals(1, get.keyy);
            assertEquals("new Name", get.valuee);
        }
    }

    /**
     * Tests the Delete method
     *
     * @throws DBException
     */
    @org.junit.Test
    public void testDelete() throws DBException {
        try (Connection conn = ConnectionFactory.transaction()) {
            Test obj = new Test();
            obj.keyy = 1;
            new GenericDAO<>(conn, Test.class).delete(obj);
            conn.commit();
            // Asserts it will return an object equal
            List<Test> list = new GenericDAO<Test>(conn, Test.class).select();
            assertTrue(list.isEmpty());
        }
    }

    /**
     * Just for testing purposes.
     */
    private static class Test {

        public Test() {
        }

        private int keyy;
        private String valuee;

        public int getKeyy() {
            return keyy;
        }

        public void setKeyy(int keyy) {
            this.keyy = keyy;
        }

        public String getValuee() {
            return valuee;
        }

        public void setValuee(String valuee) {
            this.valuee = valuee;
        }

        @Override
        public String toString() {
            return "Test{" + "chave=" + keyy + ", valor=" + valuee + '}';
        }

    }

}
