/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import java.lang.reflect.Field;
import java.util.List;

/**
 * SQL Builder class
 *
 * @author joaovperin
 */
public class SQLBuilder {

    /** Bean Class */
    private final Class<?> beanClass;

    /**
     * Constructor
     *
     * @param beanClass
     */
    public SQLBuilder(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * Builds the Select Statement
     *
     * @return String
     */
    public final String buildSelectStmt() {
        return new StringBuilder(256).
                append("SELECT ").
                append(buildSqlFields()).
                append(" FROM ").
                append(beanClass.getSimpleName()).
                toString();
    }

    /**
     * Builds the Insert Statement
     *
     * @return String
     */
    public final String buildInsertStmt() {
        StringBuilder sb = new StringBuilder(256).
                append("INSERT INTO ").
                append(beanClass.getSimpleName()).
                append(" (").append(buildSqlFields()).append(") ").
                append(" VALUES (");
        // For all declared fields
        Field[] fields = beanClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            sb.append('?');
            if (i < fields.length - 1) {
                sb.append(", ");
            }
        }
        return sb.append(")").toString();
    }

    /**
     * Builds the Update Statement
     *
     * @return String
     */
    public final String buildUpdateStmt() {
        StringBuilder sb = new StringBuilder(256).
                append("UPDATE ").
                append(beanClass.getSimpleName()).
                append(" SET ");
        // For all declared fields
        Field[] fields = beanClass.getDeclaredFields();
        for (int i = 1; i < fields.length; i++) {
            Field f = fields[i];
            sb.append(f.getName()).append(" = ?");
            if (i < fields.length - 1) {
                sb.append(", ");
            }
        }
        // First field is the primary key
        return sb.toString();
    }

    /**
     * Builds the Delete Statement
     *
     * @return String
     */
    public final String buildDeleteStmt() {
        return new StringBuilder(256).
                append("DELETE FROM ").
                append(beanClass.getSimpleName()).
                toString();
    }

    /**
     * Returns the Where statement based on a bean's Primary Key
     *
     * @return String
     */
    public final String buildWhereStmt() {
        // First field is the primary key
        Field[] fields = beanClass.getDeclaredFields();
        return new StringBuilder(256).append(" WHERE").
                append(String.format(" %s", fields[0].getName())).
                append(" = ?").
                toString();
    }

    /**
     * Builds the Where Statement based on a filter
     *
     * @param filter
     * @return String
     */
    public final String buildWhereStmt(Filter filter) {
        StringBuilder sb = new StringBuilder(256);
        if (!filter.isEmpty()) {
            sb.append(" WHERE ");
        }
        // List of filters
        List<FilterItem> get = filter.get();
        int len = get.size();
        for (int i = 0; i < len; i++) {
            FilterItem item = get.get(i);
            // If it's an operator or an expression, only append it's value
            if (item.isOperator() || item.isExpression()) {
                sb.append(item.getValue());
            } else {
                sb.append(item.getField()).
                        append(item.getCondition()).
                        append("'").append(item.getValue()).append("'");
            }
        }
        return sb.toString();
    }

    /**
     * Builds the SQL for the fields
     *
     * @return String
     */
    public final String buildSqlFields() {
        StringBuilder sb = new StringBuilder();
        Field[] fields = beanClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            sb.append(field.getName());
            if (i < fields.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

}
