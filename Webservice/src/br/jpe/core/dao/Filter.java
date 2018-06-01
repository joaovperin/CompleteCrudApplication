/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import java.util.List;

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
     * Adds a filter on a field
     *
     * @param field
     * @param condition
     * @param value
     */
    public void add(String field, FilterCondition condition, String value);

    /**
     * Adds a filter on a field
     *
     * @param field
     * @param condition
     * @param value
     */
    public void add(String field, FilterCondition condition, char value);

    /**
     * Adds a filter on a field
     *
     * @param field
     * @param condition
     * @param value
     */
    public void add(String field, FilterCondition condition, int value);

    /**
     * Adds a filter on a field
     *
     * @param field
     * @param condition
     * @param value
     */
    public void add(String field, FilterCondition condition, long value);

    /**
     * Adds a filter operator
     *
     * @param operator
     */
    public void addOperator(FilterOperator operator);

    /**
     * Adds a "LIMIT" clause
     *
     * @param recordsCount
     */
    public void addLimit(long recordsCount);

    /**
     * Returns a copy of the filter itens list
     *
     * @return List
     */
    public List<FilterItem> get();

    /**
     * Returns true if the filter is empty
     *
     * @return boolean
     */
    public boolean isEmpty();

}
