/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import br.jpe.core.database.connection.Connection;
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
            List<Object> list = new GenericDAO<>(conn, Test.class).select();
            list.forEach((t) -> {
                assertNotNull(t);
            });
        }
    }

    /**
     * Tests the select method with filters
     *
     * @throws DBException
     */
    @org.junit.Test
    public void testSelectWithFilters() throws DBException {
        try (Connection conn = ConnectionFactory.transaction()) {
            GenericDAO<Test> dao = new GenericDAO<>(conn, Test.class);
            // Inserts some dummy temporary data (it will rollback after)
            dao.insert(new Test(11, "foo"));
            dao.insert(new Test(12, "bar"));
            dao.insert(new Test(13, "foobar"));
            // OR filter testing
            Filter filter = new GenericFilter();
            filter.add("keyy", FilterCondition.EQUAL, "11");
            filter.addOperator(FilterOperator.OR);
            filter.add("keyy", FilterCondition.EQUAL, "13");
            // OR filter asserts
            List<Test> list = dao.select(filter);
            assertEquals(2, list.size());
            assertNotNull(list.stream().filter((e) -> e.keyy == 11).findAny().get());
            assertNotNull(list.stream().filter((e) -> e.keyy == 13).findAny().get());

            // AND filter testing
            filter = new GenericFilter();
            filter.add("keyy", FilterCondition.EQUAL, "12");
            filter.addOperator(FilterOperator.AND);
            filter.add("valuee", FilterCondition.EQUAL, "bar");
            // OR filter asserts
            list = dao.select(filter);
            assertEquals(1, list.size());
            assertNotNull(list.stream().filter((e) -> e.keyy == 12 && e.valuee.equals("bar")).findAny().get());

            // Expression filter testing
            filter = new GenericFilter();
            filter.begin();
            filter.add("keyy", FilterCondition.EQUAL, "12");
            filter.addOperator(FilterOperator.AND);
            filter.add("valuee", FilterCondition.EQUAL, "bar");
            filter.end();
            filter.addOperator(FilterOperator.OR);
            filter.add("keyy", FilterCondition.EQUAL, "13");

            // Expression filter asserts
            list = dao.select(filter);
            assertEquals(2, list.size());
            assertNotNull(list.stream().filter((e) -> e.keyy == 12 && e.valuee.equals("bar")).findAny().get());
            assertNotNull(list.stream().filter((e) -> e.keyy == 13).findAny().get());
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
            GenericDAO<Test> dao = new GenericDAO<>(conn, Test.class);
            Test obj = new Test(1, "new Name");
            dao.update(obj);
            conn.commit();
            // Asserts it's changed
            assertEquals(1, obj.keyy);
            assertEquals("new Name", obj.valuee);
            // Asserts it will return an object equal
            Test get = dao.select().get(0);
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
            GenericDAO<Test> dao = new GenericDAO<>(conn, Test.class);
            dao.delete(obj);
            conn.commit();
            // Asserts it will return an object equal
            List<Test> list = dao.select();
            assertTrue(list.isEmpty());
        }
    }

    /**
     * Just for testing purposes.
     */
    private static class Test {

        public Test() {
        }

        public Test(int keyy, String valuee) {
            this.keyy = keyy;
            this.valuee = valuee;
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
