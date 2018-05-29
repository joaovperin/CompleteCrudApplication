/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import br.jpe.core.database.Connection;
import br.jpe.core.database.ConnectionFactory;
import br.jpe.core.database.DBException;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for the DAOFactory
 *
 * @author joaovperin
 */
public class DAOFactoryTest {

    /**
     * Ensures it's throwing a NPE when connection is null
     *
     * @throws br.jpe.core.database.DBException
     */
    @Test(expected = NullPointerException.class)
    public void testNullConnection() throws DBException {
        DataAccessObject<DummyEntity> create = DAOFactory.create(DummyEntity.class, null);
        assertEquals(GenericDAO.class, create.getClass());
    }

    /**
     * Tests the creation of a generic DAO
     *
     * @throws br.jpe.core.database.DBException
     */
    @Test
    public void testGenericDAO() throws DBException {
        try (Connection conn = ConnectionFactory.query()) {
            DataAccessObject<DummyEntity> create = DAOFactory.create(DummyEntity.class, conn);
            assertEquals(GenericDAO.class, create.getClass());
        }
    }

    // Dummy entity
    private class DummyEntity {

    }

}
