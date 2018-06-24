/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import br.jpe.core.dao.testing.TestBean;
import br.jpe.core.database.sql.ConnectionFactory;
import br.jpe.core.database.DBException;
import br.jpe.core.database.sql.connection.SQLConnection;
import java.util.List;
import static org.junit.Assert.*;

/**
 * GenericDAO Test class
 *
 * @author joaovperin
 */
public class GenericDAOTestDB {

    /**
     * Tests the select method
     *
     * @throws DBException
     */
    @org.junit.Test
    public void testSelect() throws DBException {
        TestBean bean = new TestBean(1, "foo");
        try (SQLConnection conn = ConnectionFactory.transaction()) {
            GenericDAO<TestBean> dao = new GenericDAO<>(conn, TestBean.class);
            dao.insert(bean);

            List<TestBean> list = dao.findAll();
            list.forEach((t) -> assertNotNull(t));
            assertTrue(list.contains(bean));
        }
    }

    /**
     * Tests the select method with filters
     *
     * @throws DBException
     */
    @org.junit.Test
    public void testSelectWithFilters() throws DBException {
        try (SQLConnection conn = ConnectionFactory.transaction()) {
            GenericDAO<TestBean> dao = new GenericDAO<>(conn, TestBean.class);
            // Inserts some dummy temporary data (it will rollback after)
            dao.insert(new TestBean(11, "foo"));
            dao.insert(new TestBean(12, "bar"));
            dao.insert(new TestBean(13, "foobar"));

            // OR filter testing
            Filter filter = new GenericFilter();
            filter.add("keyy", FilterCondition.EQUAL, "11");
            filter.addOperator(FilterOperator.OR);
            filter.add("keyy", FilterCondition.EQUAL, "13");
            // OR filter asserts
            List<TestBean> list = dao.findAll(filter);
            assertEquals(2, list.size());
            assertNotNull(list.stream().filter((e) -> e.keyy == 11).findAny().get());
            assertNotNull(list.stream().filter((e) -> e.keyy == 13).findAny().get());

            // AND filter testing
            filter = new GenericFilter();
            filter.add("keyy", FilterCondition.EQUAL, "12");
            filter.addOperator(FilterOperator.AND);
            filter.add("valuee", FilterCondition.EQUAL, "bar");
            // AND filter asserts
            list = dao.findAll(filter);
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
            list = dao.findAll(filter);
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
        TestBean obj = new TestBean();
        try (SQLConnection cn = ConnectionFactory.transaction()) {
            obj.setKeyy(0);
            obj.setValuee("useless name here");
            new GenericDAO<>(cn, TestBean.class).insert(obj);
        }
        // Asserts it will get the auto-incremented key
        assertNotEquals(0, obj.keyy);
    }

    /**
     * Tests the Update method
     *
     * @throws DBException
     */
    @org.junit.Test
    public void testUpdate() throws DBException {
        try (SQLConnection conn = ConnectionFactory.transaction()) {
            GenericDAO<TestBean> dao = new GenericDAO<>(conn, TestBean.class);

            final int code = 39;
            // Inserts a dummy testing record with code 39
            TestBean obj = new TestBean(code, "old name");
            dao.insert(obj);

            // Updates the code 39 record value
            obj = new TestBean(code, "new Name");
            dao.update(obj);

            // Asserts it's changed
            assertEquals(code, obj.keyy);
            assertEquals("new Name", obj.valuee);

            Filter filter = new GenericFilter();
            filter.add("keyy", FilterCondition.EQUAL, code);

            // Asserts it will return an object equal
            TestBean get = dao.findOne(filter);
            assertEquals(code, get.keyy);
            assertEquals("new Name", get.valuee);
        }
    }

    /**
     * Tests the UpdateAll method
     *
     * @throws DBException
     */
    @org.junit.Test
    public void testUpdateAll() throws DBException {
        try (SQLConnection conn = ConnectionFactory.transaction()) {
            GenericDAO<TestBean> dao = new GenericDAO<>(conn, TestBean.class);

            dao.insert(new TestBean(3, "haha"));
            dao.insert(new TestBean(4, "hehe"));
            dao.insert(new TestBean(5, "hihi"));

            GenericFilter filter = new GenericFilter();
            filter.add("keyy", FilterCondition.EQUAL, "3");
            filter.addOperator(FilterOperator.OR);
            filter.add("keyy", FilterCondition.EQUAL, "5");

            dao.updateAll(new TestBean(4, "new Name"), filter);

            // Asserts it will return an object equal
            List<TestBean> list = dao.findAll(filter);
            assertEquals(2, list.size());
            assertEquals(3, list.get(0).keyy);
            assertEquals(5, list.get(1).keyy);
            list.forEach((e) -> assertEquals("new Name", e.valuee));
        }
    }

    /**
     * Tests the Delete method
     *
     * @throws DBException
     */
    @org.junit.Test
    public void testDelete() throws DBException {
        try (SQLConnection conn = ConnectionFactory.transaction()) {
            TestBean obj = new TestBean();
            obj.keyy = 1;
            GenericDAO<TestBean> dao = new GenericDAO<>(conn, TestBean.class);
            dao.delete(obj);
            // Asserts it will return an object equal
            List<TestBean> list = dao.findAll();
            assertFalse(list.contains(obj));
        }
    }

    /**
     * Tests the DeleteAll method
     *
     * @throws DBException
     */
    @org.junit.Test
    public void testDeleteAll() throws DBException {
        try (SQLConnection conn = ConnectionFactory.transaction()) {
            GenericDAO<TestBean> dao = new GenericDAO<>(conn, TestBean.class);
            // Cleans up the database
            dao.deleteAll();

            // Adds 3 records
            dao.insert(new TestBean(3, "haha"));
            TestBean bean2 = new TestBean(4, "hehe");
            dao.insert(bean2);
            dao.insert(new TestBean(5, "hihi"));

            // Creates a filter for 2 of the 3 inserted records
            GenericFilter filter = new GenericFilter();
            filter.add("keyy", FilterCondition.EQUAL, "3");
            filter.addOperator(FilterOperator.OR);
            filter.add("keyy", FilterCondition.EQUAL, "5");

            // Deletes registers 3 and 5, so it will only left the 4
            dao.deleteAll(filter);  // TODO: VERIFYY THAT

            // Asserts it will return an object equal
            List<TestBean> list = dao.findAll();
            assertEquals(1, list.size());
            assertEquals(4, list.get(0).keyy);
            assertTrue(list.get(0).equals(bean2));
            assertTrue(list.contains(bean2));
        }
    }

}
