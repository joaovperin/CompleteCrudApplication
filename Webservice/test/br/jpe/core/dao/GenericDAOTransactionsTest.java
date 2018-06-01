/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import br.jpe.core.dao.testing.TestBean;
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
public class GenericDAOTransactionsTest {

    /**
     * Setup database before executing the tests
     *
     * @throws DBException
     * @throws SQLException
     */
    @BeforeClass
    public static void setUpDatabase() throws DBException, SQLException {
        try (Connection conn = ConnectionFactory.transaction()) {
            DBUtils.createTestDB(conn);
            conn.commit();
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
        try (Connection conn = ConnectionFactory.transaction()) {
            DBUtils.deleteTestDB(conn);
            conn.commit();
        }
    }

    /**
     * Tests commit and read on separate connection
     *
     * @throws DBException
     */
    @org.junit.Test
    public void testCommitOnSeparateConnections() throws DBException {
        TestBean bean = new TestBean(1, "foo");
        // Includes a register
        try (Connection conn = ConnectionFactory.transaction()) {
            new GenericDAO<>(conn, TestBean.class).insert(bean);
            conn.commit();
        }
        // Tests select
        try (Connection conn = ConnectionFactory.query()) {
            List<TestBean> list = new GenericDAO<TestBean>(conn, TestBean.class).select();
            assertTrue(list.contains(bean));
            list.forEach((t) -> assertNotNull(t));
        }
    }

}
