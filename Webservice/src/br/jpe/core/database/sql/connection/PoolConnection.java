/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.database.sql.connection;

/**
 * A class to represent a connection from the pool
 *
 * @author joaovperin
 */
public interface PoolConnection extends SQLConnection {

    /**
     * Returns true if the connection is free to be used
     *
     * @return boolean
     */
    public boolean isFree();

    /**
     * Indicates a connection is busy and cannot be used
     */
    public void busy();

    /**
     * Free a connection to be used
     */
    public void free();

    /**
     * Closes the Jdbc Connection
     */
    public void jdbcClose();

}
