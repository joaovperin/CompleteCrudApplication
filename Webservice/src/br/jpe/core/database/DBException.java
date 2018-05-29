/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.database;

/**
 * Database Exception
 *
 * @author joaovperin
 */
public class DBException extends Exception {

    /**
     * Constructor
     *
     * @param string
     */
    public DBException(String string) {
        super(string);
    }

    /**
     * Constructor
     *
     * @param thrwbl
     */
    public DBException(Throwable thrwbl) {
        super(thrwbl);
    }

    /**
     * Constructor
     *
     * @param string
     * @param thrwbl
     */
    public DBException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

}
