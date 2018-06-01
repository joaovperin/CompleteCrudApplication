/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a generic filter
 *
 * @joaovperin
 */
public class GenericFilter implements Filter {

    /** Begin of an expression mark */
    private static final String BEGIN_EXPRESSION = "(";
    /** End of an expression mark */
    private static final String END_EXPRESSION = ")";
    /** SQL Limit clause */
    private static final String LIMIT_CLAUSE = " LIMIT ";

    /** List of filters */
    private final List<FilterItem> list;

    /**
     * Default constructor
     */
    public GenericFilter() {
        this.list = new ArrayList<>();
    }

    /**
     * Constructs the object based on another filter
     *
     * @param filter
     */
    public GenericFilter(Filter filter) {
        this.list = filter.get();
    }

    /**
     * Begins an expression
     */
    @Override
    public void begin() {
        list.add(new FilterItem(BEGIN_EXPRESSION));
    }

    /**
     * Ends an expression
     */
    @Override
    public void end() {
        list.add(new FilterItem(END_EXPRESSION));
    }

    /**
     * Adds a filter on a field
     *
     * @param field
     * @param condition
     * @param value
     */
    @Override
    public void add(String field, FilterCondition condition, String value) {
        list.add(new FilterItem(field, condition, value));
    }

    /**
     * Adds a filter on a field
     *
     * @param field
     * @param condition
     * @param value
     */
    @Override
    public void add(String field, FilterCondition condition, int value) {
        this.add(field, condition, String.valueOf(value));
    }

    /**
     * Adds a filter on a field
     *
     * @param field
     * @param condition
     * @param value
     */
    @Override
    public void add(String field, FilterCondition condition, long value) {
        this.add(field, condition, String.valueOf(value));
    }

    /**
     * Adds a filter on a field
     *
     * @param field
     * @param condition
     * @param value
     */
    @Override
    public void add(String field, FilterCondition condition, char value) {
        this.add(field, condition, String.valueOf(value));
    }

    /**
     * Adds a filter operator
     *
     * @param operator
     */
    @Override
    public void addOperator(FilterOperator operator) {
        list.add(new FilterItem(operator));
    }

    /**
     * Adds a "LIMIT" clause
     *
     * @param recordsCount
     */
    public void addLimit(long recordsCount) {
        list.add(new FilterItem(LIMIT_CLAUSE.concat(String.valueOf(recordsCount))));
    }

    /**
     * Returns a copy of the list
     *
     * @return List
     */
    @Override
    public List<FilterItem> get() {
        return new ArrayList<>(list);
    }

    /**
     * Returns true if the filter list is empty
     *
     * @return booleanI
     */
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

}
