/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import br.jpe.core.utils.TextX;
import br.jpe.core.database.DBException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import br.jpe.core.database.Connection;
import java.sql.PreparedStatement;

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
    protected final Connection conn;

    /**
     * Constructor that receives the connection
     *
     * @param conn
     * @param beanClass
     */
    public GenericDAO(Connection conn, Class<?> beanClass) {
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
     * INSERT a register on the database and returns true if it works
     *
     * @param bean
     * @return List
     * @throws DBException
     */
    @Override
    public boolean insert(B bean) throws DBException {
        try (PreparedStatement pstm = conn.prepareStmt(buildInsertStmt())) {
            Field[] fields = beanClass.getDeclaredFields();
            try {
                int i = 1;
                for (Field field : fields) {
                    Object value = getGetter(field).invoke(bean);
                    pstm.setObject(i++, value);
                }
            } catch (ReflectiveOperationException e) {
                throw new DBException(e);
            }
            // Executes the update and retrieve generated keys (if auto increment)
            int executeUpdate = pstm.executeUpdate();
            ResultSet rs = pstm.getGeneratedKeys();
            int i = 0;
            while (rs.next()) {
                Field field = fields[i++];
                Method getter = getGetterFromRs(field);
                getSetter(field).invoke(bean, getter.invoke(rs, i));
            }
            return executeUpdate > 0;
        } catch (DBException | SQLException | ReflectiveOperationException e) {
            throw new DBException(e);
        }
    }

    @Override
    public boolean update(B bean) throws DBException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(B bean) throws DBException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            return beanClass.getMethod("set" + TextX.capitalize(field.getName()), field.getType());
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new ReflectiveOperationException(ex);
        }
    }

    /**
     * Get the appropriate bean Getter method
     *
     * @param field
     * @return Method
     * @throws java.lang.ReflectiveOperationException
     */
    public Method getGetter(Field field) throws ReflectiveOperationException {
        try {
            Class<?> type = field.getType();
            String prefix = type.equals(Boolean.class) ? "is" : "get";
            return beanClass.getMethod(prefix + TextX.capitalize(field.getName()));
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
            return ResultSet.class.getMethod("get" + TextX.capitalize(field.getType().getSimpleName()), int.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new ReflectiveOperationException(ex);
        }
    }

    /**
     * Builds the Select Statement
     *
     * @return String
     */
    private String buildSelectStmt() {
        return new StringBuilder(256).
                append("SELECT ").
                append(buildSqlFields()).
                append(" FROM ").
                append(beanClass.getSimpleName()).
                toString();
    }

    /**
     * Builds the Select Statement
     *
     * @return String
     */
    private String buildInsertStmt() {
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
     * Builds the SQL for the fields
     *
     * @return String
     */
    private String buildSqlFields() {
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
