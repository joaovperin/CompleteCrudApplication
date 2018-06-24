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

    /**
     * Returns the database type
     *
     * @param name
     * @return DatabaseType
     */
    public static DatabaseType forName(String name) {
        // Defaults to SQL
        if (name == null || name.trim().isEmpty()) {
            return DatabaseType.SQL;
        }
        // Serch on the enum values
        for (DatabaseType type : values()) {
            if (type.valueName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new UnsupportedOperationException("DatabaseType '" + name + "' not supported (yet).");
    }

}
