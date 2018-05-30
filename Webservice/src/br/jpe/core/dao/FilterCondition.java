/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

/**
 * Filter Condition
 *
 * @author joaovperin
 */
public enum FilterCondition {

    EQUAL("=");

    /** SQL */
    private final String sql;

    /**
     * Constructor
     *
     * @param sql
     */
    private FilterCondition(String sql) {
        this.sql = sql;
    }

    /**
     * Gets the SQL
     *
     * @return String
     */
    @Override
    public String toString() {
        return sql;
    }

}
