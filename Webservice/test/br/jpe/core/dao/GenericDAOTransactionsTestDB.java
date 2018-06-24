/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import br.jpe.core.dao.testing.TestBean;
import br.jpe.core.database.Connection;
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
public class GenericDAOTransactionsTestDB {

    /**
     * Tests commit and read on separate connection
     *
     * @throws DBException
     */
    @org.junit.Test
    public void testCommitOnSeparateConnections() throws DBException {
        TestBean bean = new TestBean(58, "foo");
        // Includes a register
        try (Connection conn = ConnectionFactory.transaction()) {
            new GenericDAO<>((SQLConnection) conn, TestBean.class).insert(bean);
            conn.commit();
        }
        // Tests select
        try (Connection conn = ConnectionFactory.query()) {
            List<TestBean> list = new GenericDAO<>((SQLConnection) conn, TestBean.class).findAll();
            assertTrue(list.contains(bean));
            list.forEach((t) -> assertNotNull(t));
        }
    }

}
