/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.database;

/**
 * Database known types
 *
 * @author joaovperin
 */
public enum DatabaseType {

    /** Standard Query Language Database */
    SQL("sql");

    /** Database type property name */
    private final String valueName;

    /**
     * Constructor
     *
     * @param valueName
     */
    private DatabaseType(String valueName) {
        this.valueName = valueName;
    }

    /**
     * Returns the value name
     *
     * @return String
     */
    public String valueName() {
        return valueName;
    }

}
