/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;

/**
 * Reflection utilities
 *
 * @author joaovperin
 */
public class ReflectionX {

    /** Preffix of SET method */
    private static final String SET_PREFIX = "set";
    /** Preffix of GET method */
    private static final String GET_PREFIX = "get";
    /** Preffix of IS method */
    private static final String IS_PREFIX = "is";

    /**
     * Creates a new Instance of a class
     *
     * @param clazz Class to instantiate
     * @param <B> Class type
     * @return B Instance of the provided class
     * @throws java.lang.ReflectiveOperationException
     */
    public static <B> B instantiate(Class<B> clazz) throws ReflectiveOperationException {
        try {
            return (B) clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new ReflectiveOperationException(e);
        }
    }

    /**
     * Get the appropriate bean Setter method
     *
     * @param clazz
     * @param field
     * @return Method
     * @throws java.lang.ReflectiveOperationException
     */
    public static Method setter(Class clazz, Field field) throws ReflectiveOperationException {
        try {
            return clazz.getMethod(SET_PREFIX.concat(TextX.capitalize(field.getName())), field.getType());
        } catch (NoSuchMethodException | SecurityException e) {
            throw new ReflectiveOperationException(e);
        }
    }

    /**
     * Get the appropriate bean Getter method
     *
     * @param clazz
     * @param field
     * @return Method
     * @throws java.lang.ReflectiveOperationException
     */
    public static Method getter(Class clazz, Field field) throws ReflectiveOperationException {
        try {
            String prefix = isBoolean(field) ? IS_PREFIX : GET_PREFIX;
            return clazz.getMethod(prefix + TextX.capitalize(field.getName()));
        } catch (NoSuchMethodException | SecurityException e) {
            throw new ReflectiveOperationException(e);
        }
    }

    /**
     * Get the ResultSet Getter method for an Integer value
     *
     * @param field
     * @return Method
     * @throws ReflectiveOperationException
     */
    public static Method getterResultSetInt(Field field) throws ReflectiveOperationException {
        return getterResultSet(field, int.class);
    }

    /**
     * Get the ResultSet appropriate Getter method
     *
     * @param field
     * @param typeClazz
     * @return Method
     * @throws ReflectiveOperationException
     */
    public static Method getterResultSet(Field field, Class typeClazz) throws ReflectiveOperationException {
        try {
            return ResultSet.class.getMethod(GET_PREFIX + TextX.capitalize(field.getType().getSimpleName()), typeClazz);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new ReflectiveOperationException(e);
        }
    }

    /**
     * Returns true if the type is boolean
     *
     * @param type
     * @return boolean
     */
    private static boolean isBoolean(Field field) {
        if (field == null) {
            return false;
        }
        Class<?> type = field.getType();
        return type.equals(boolean.class) || type.equals(Boolean.class);
    }

}
