/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import br.jpe.core.database.Conexao;
import br.jpe.core.database.DBException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A Generic Data Access Object
 *
 * @author joaovperin
 * @param <B> Bean class
 */
public class GenericDAO<B> implements DataAccessObject<B> {

    /** Bean Class */
    private final Class<?> beanClass;
    /** Connection to the database */
    protected final Conexao conn;

    /**
     * Constructor that receives the connection
     *
     * @param conn
     * @param beanClass
     */
    public GenericDAO(Conexao conn, Class<?> beanClass) {
        this.beanClass = beanClass;
        this.conn = Objects.requireNonNull(conn, "A valid connection is needed.");
    }

    /**
     * Executes a SELECT statement on the database and returns a list of beans
     *
     * @return List
     * @throws DBException
     */
    @Override
    public List<B> select() throws DBException {
        List<B> list = new ArrayList<>();
        try (ResultSet rs = conn.createStmt().executeQuery(buildSelectStmt())) {
            while (rs.next()) {
                try {
                    B bean = createBean();
                    int i = 1;
                    for (Field field : beanClass.getDeclaredFields()) {
                        Object value = getGetterFromRs(field).invoke(rs, i++);
                        getSetter(field).invoke(bean, value);
                    }
                    list.add(bean);
                } catch (ReflectiveOperationException e) {
                    throw new DBException(e);
                }
            }
        } catch (DBException | SQLException e) {
            throw new DBException(e);
        }
        return list;
    }

    /**
     * Create a new Bean Instance
     *
     * @return B
     * @throws java.lang.ReflectiveOperationException
     */
    public B createBean() throws ReflectiveOperationException {
        try {
            return (B) beanClass.newInstance();
        } catch (IllegalAccessException | InstantiationException ex) {
            throw new ReflectiveOperationException(ex);
        }
    }

    /**
     * Get the appropriate bean Setter method
     *
     * @param field
     * @return Method
     * @throws java.lang.ReflectiveOperationException
     */
    public Method getSetter(Field field) throws ReflectiveOperationException {
        try {
            return beanClass.getMethod("set" + capitalize(field.getName()), field.getType());
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new ReflectiveOperationException(ex);
        }
    }

    /**
     * Get the ResultSet appropriate Getter method
     *
     * @param field
     * @return Method
     * @throws ReflectiveOperationException
     */
    public Method getGetterFromRs(Field field) throws ReflectiveOperationException {
        try {
            return ResultSet.class.getMethod("get" + capitalize(field.getType().getSimpleName()), int.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new ReflectiveOperationException(ex);
        }
    }

    /**
     * Capitalizes a field
     *
     * @param input
     * @return String
     */
    private String capitalize(String input) {
        return input.substring(0, 1).toUpperCase().concat(input.substring(1, input.length()));
    }

    /**
     * Build the Select Statement
     *
     * @return String
     */
    private String buildSelectStmt() {
        StringBuilder sb = new StringBuilder(256);
        sb.append("SELECT ");
        Field[] fields = beanClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            sb.append(field.getName());
            if (i < fields.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(" FROM ").append(beanClass.getSimpleName());
        return sb.toString();
    }

    @Override
    public boolean insert(B bean) throws DBException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean update(B bean) throws DBException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(B bean) throws DBException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
