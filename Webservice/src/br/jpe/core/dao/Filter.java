/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

/**
 * An object that represents a filter for the database
 *
 * @author joaovperin
 */
public interface Filter {

    /**
     * Begins an expression
     */
    public void begin();

    /**
     * Ends an expression
     */
    public void end();

    /**
     * Adds a filter
     *
     * @param field
     * @param condition
     * @param value
     */
    public void add(String field, FilterCondition condition, String value);

    /**
     * Build the filter into an SQL command
     *
     * @return String
     */
    public String build();

}
