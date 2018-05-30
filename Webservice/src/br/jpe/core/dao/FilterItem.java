/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

/**
 * A class that represents a Filter Item
 *
 * @author joaovperin
 */
public class FilterItem {

    /** Name of the database field */
    private final String field;
    /** Condition to be used */
    private final FilterCondition condition;
    /** Value to filter */
    private final String value;

    /**
     * Constructor
     *
     * @param field
     * @param condition
     * @param value
     */
    public FilterItem(String field, FilterCondition condition, String value) {
        this.field = field;
        this.condition = condition;
        this.value = value;
    }

    /**
     * Returns the field name
     *
     * @return String
     */
    public String getField() {
        return field;
    }

    /**
     * Returns the Condition
     *
     * @return FilterCondition
     */
    public FilterCondition getCondition() {
        return condition;
    }

    /**
     * Returns the value
     *
     * @return String
     */
    public String getValue() {
        return value;
    }

}
