/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the ReflectionX utils
 *
 * @author joaovperin
 */
public class ReflectionXTest {

    /**
     * Test of instantiate method, of class ReflectionX.
     *
     * @throws java.lang.ReflectiveOperationException
     */
    @Test
    public void testInstantiate() throws ReflectiveOperationException {
        Foo instantiate = ReflectionX.instantiate(Foo.class);
        assertTrue((instantiate instanceof Foo));
    }

    /**
     * Test of setter method, of class ReflectionX.
     *
     * @throws java.lang.ReflectiveOperationException
     */
    @Test
    public void testSetter() throws ReflectiveOperationException {
        // String field
        Field field = Foo.class.getDeclaredField("name");
        Method expResult = Foo.class.getDeclaredMethod("setName", String.class);
        assertEquals(expResult, ReflectionX.setter(Foo.class, field));
    }

    /**
     * Test of getter method, of class ReflectionX.
     *
     * @throws java.lang.ReflectiveOperationException
     */
    @Test
    public void testGetter() throws ReflectiveOperationException {
        // Non-boolean field
        Field field = Foo.class.getDeclaredField("code");
        Method expResult = Foo.class.getDeclaredMethod("getCode");
        assertEquals(expResult, ReflectionX.getter(Foo.class, field));
        // Boolean field
        field = Foo.class.getDeclaredField("bar");
        expResult = Foo.class.getDeclaredMethod("isBar");
        assertEquals(expResult, ReflectionX.getter(Foo.class, field));
    }

    /**
     * Test of getterResultSetInt method, of class ReflectionX.
     *
     * @throws java.lang.ReflectiveOperationException
     */
    @Test
    public void testGetterResultSetInt() throws ReflectiveOperationException {
        // Integer field
        Field field = Foo.class.getDeclaredField("code");
        Method expResult = ResultSet.class.getDeclaredMethod("getInt", int.class);
        assertEquals(expResult, ReflectionX.getterResultSetInt(field));
        // String field
        field = Foo.class.getDeclaredField("name");
        expResult = ResultSet.class.getDeclaredMethod("getString", int.class);
        assertEquals(expResult, ReflectionX.getterResultSetInt(field));
        // Boolean field
        field = Foo.class.getDeclaredField("bar");
        expResult = ResultSet.class.getDeclaredMethod("getBoolean", int.class);
        assertEquals(expResult, ReflectionX.getterResultSetInt(field));
    }

    /**
     * Test of getterResultSet method, of class ReflectionX.
     *
     * @throws java.lang.ReflectiveOperationException
     */
    @Test
    public void testGetterResultSet() throws ReflectiveOperationException {
        // Integer field
        Field field = Foo.class.getDeclaredField("code");
        Method expResult = ResultSet.class.getDeclaredMethod("getInt", String.class);
        assertEquals(expResult, ReflectionX.getterResultSet(field, String.class));
        // String field
        field = Foo.class.getDeclaredField("name");
        expResult = ResultSet.class.getDeclaredMethod("getString", String.class);
        assertEquals(expResult, ReflectionX.getterResultSet(field, String.class));
        // Boolean field
        field = Foo.class.getDeclaredField("bar");
        expResult = ResultSet.class.getDeclaredMethod("getBoolean", String.class);
        assertEquals(expResult, ReflectionX.getterResultSet(field, String.class));
    }

    /**
     * Dummy class for tests
     */
    public static class Foo {

        private int code;
        private String name;
        private boolean bar;

        //---------------------//
        // Getters and Setters //
        //---------------------//
        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isBar() {
            return bar;
        }

        public void setBar(boolean bar) {
            this.bar = bar;
        }

    }

}
