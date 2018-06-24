/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.database;

/**
 * An interface for Connections
 *
 * @author joaovperin
 */
public interface Connection extends AutoCloseable {

    /**
     * Commit the connection
     *
     * @throws DBException
     */
    public void commit() throws DBException;

    /**
     * Rollbacks the connection
     *
     * @throws DBException
     */
    public void rollback() throws DBException;

    /**
     * Closes the connection
     *
     * @throws DBException
     */
    @Override
    public void close() throws DBException;

}
