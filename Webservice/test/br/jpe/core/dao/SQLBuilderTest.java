/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the SQL Builder
 *
 * @author joaovperin
 */
public class SQLBuilderTest {

    /** Instance of the builder */
    final SQLBuilder sql;

    /**
     * Construtor
     */
    public SQLBuilderTest() {
        this.sql = new SQLBuilder(FooBar.class);
    }

    @Test
    public void testInsertCommand() {
        String cmd = sql.buildInsertStmt();
        Assert.assertEquals("INSERT INTO FooBar (foo, bar, foobar)  VALUES (?, ?, ?)", cmd);
    }

    @Test
    public void testSelectCommand() {
        String cmd = sql.buildSelectStmt();
        Assert.assertEquals("SELECT foo, bar, foobar FROM FooBar", cmd);
    }

    @Test
    public void testUpdateCommand() {
        String cmd = sql.buildUpdateStmt();
        Assert.assertEquals("UPDATE FooBar SET bar = ?, foobar = ?", cmd);
    }

    @Test
    public void testDeleteCommand() {
        String cmd = sql.buildDeleteStmt();
        Assert.assertEquals("DELETE FROM FooBar", cmd);
    }

    @Test
    public void testBuildFieldsStmt() {
        String cmd = sql.buildSqlFields();
        Assert.assertEquals("foo, bar, foobar", cmd);
    }

    @Test
    public void testBuildWhereStmtClass() {
        String cmd = sql.buildWhereStmt();
        Assert.assertEquals(" WHERE foo = ?", cmd);
    }

    @Test
    public void testBuildWhereStmtFilter() {
        // Empty filter
        Filter filter = new GenericFilter();
        String cmd = sql.buildWhereStmt(filter);
        Assert.assertEquals("", cmd);

        // Simple Filter
        filter.add("foo", FilterCondition.EQUAL, "123");
        cmd = sql.buildWhereStmt(filter);
        Assert.assertEquals(" WHERE foo='123'", cmd);

        // Multiple fields filter
        filter = new GenericFilter();
        filter.add("foo", FilterCondition.EQUAL, "123");
        filter.addOperator(FilterOperator.AND);
        filter.add("bar", FilterCondition.NOT_EQUAL, "5");
        cmd = sql.buildWhereStmt(filter);
        Assert.assertEquals(" WHERE foo='123' AND bar!='5'", cmd);

        // Filter with expressions
        filter = new GenericFilter();
        filter.begin();
        filter.add("foo", FilterCondition.EQUAL, "123");
        filter.addOperator(FilterOperator.AND);
        filter.add("bar", FilterCondition.NOT_EQUAL, "5");
        filter.end();
        filter.addOperator(FilterOperator.OR);
        filter.add("foobar", FilterCondition.EQUAL, "9");

        cmd = sql.buildWhereStmt(filter);
        Assert.assertEquals(" WHERE (foo='123' AND bar!='5') OR foobar='9'", cmd);
    }

    /**
     * Some test stuff
     */
    public static class FooBar {

        public int foo;
        public String bar;
        public long foobar;

    }

}
