/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.database;

import java.util.Properties;

/**
 * Storing and easy-access to pool properties
 *
 * @author joaovperin
 */
public final class PoolProperties {

    /** Property Name - Max connections on the pool */
    private static final String PT_MAX_CONN = "maxConnections";
    /** Property Name - Awaiting time for each try to get a free connection */
    private static final String PT_AWAIT_TIME = "awaitingTime";
    /** Property Name - Max number of times it will attempt to get a free conn */
    private static final String PT_MAX_TRIES = "maxCreateConnectionTries";

    /** Properties object */
    private final Properties properties;

    /**
     * Private constructor to ensure no one instantiate this external
     *
     * @param properties
     */
    private PoolProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * Getter of the Max Tries parameter with default value
     *
     * @return int
     */
    public int getMaxTries() {
        return Integer.valueOf(properties.getProperty(PT_MAX_TRIES, "15"));
    }

    /**
     * Getter of the Max Connections parameter with default value
     *
     * @return int
     */
    public int getMaxConnections() {
        return Integer.valueOf(properties.getProperty(PT_MAX_CONN, "10"));
    }

    /**
     * Getter of the Awaiting Time parameter with default value
     *
     * @return int
     */
    public int getAwaitingTime() {
        return Integer.valueOf(properties.getProperty(PT_AWAIT_TIME, "80"));
    }

    /**
     * A factory method to create a instance of this class
     *
     * @return PoolProperties
     */
    public static PoolProperties create() {
        return create(new Properties());
    }

    /**
     * A factory method to create a instance of this class based on
     * pre-determinated properties
     *
     * @param pt Properties
     * @return PoolProperties
     */
    public static PoolProperties create(Properties pt) {
        return new PoolProperties(pt);
    }

}
