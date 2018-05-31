/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import java.util.Objects;

/**
 * A class that represents a Filter Item
 *
 * @author joaovperin
 */
public class FilterItem {

    /** Constant to indicate it's a field */
    private static final int TYPE_FIELD = 1;
    /** Constant to indicate it's an operator */
    private static final int TYPE_OPERATOR = 2;
    /** Constant to indicate it's an expression */
    private static final int TYPE_EXPRESSION = 3;

    /** Name of the database field */
    private final String field;
    /** Condition to be used */
    private final FilterCondition condition;
    /** Value to filter */
    private final String value;
    /** Type of the filter item */
    private final int type;

    /**
     * Constructor that builds a field filter
     *
     * @param field
     * @param condition
     * @param value
     */
    public FilterItem(String field, FilterCondition condition, String value) {
        this.type = TYPE_FIELD;
        this.field = field;
        this.condition = condition;
        this.value = value;
    }

    /**
     * Constructor that builds an operator
     *
     * @param operator
     */
    public FilterItem(FilterOperator operator) {
        this.type = TYPE_OPERATOR;
        this.value = operator.toString();
        this.field = null;
        this.condition = null;
    }

    /**
     * Constructor that builds an expression
     *
     * @param expression
     */
    public FilterItem(String expression) {
        Objects.requireNonNull(expression, "Expression must have a value!");
        this.type = TYPE_EXPRESSION;
        this.value = expression;
        this.field = null;
        this.condition = null;
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

    /**
     * Returns true if it's an operator
     *
     * @return boolean
     */
    public boolean isOperator() {
        return this.type == TYPE_OPERATOR;
    }

    /**
     * Returns true if it's an operator
     *
     * @return boolean
     */
    public boolean isExpression() {
        return this.type == TYPE_EXPRESSION;
    }

}
