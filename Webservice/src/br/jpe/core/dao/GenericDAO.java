/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import br.jpe.core.database.ConnectionFactory;
import br.jpe.core.utils.TextX;
import br.jpe.core.database.DBException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import br.jpe.core.database.connection.Connection;
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
     * Executes a SELECT statement on the database and returns a list of beans based on a filter
     *
     * @param filter
     * @return List
     * @throws DBException
     */
    @Override
    public List<B> select(Filter filter) throws DBException {
        List<B> list = new ArrayList<>();
        try (PreparedStatement pstm = conn.prepareStmt(buildSelectStmt().concat(buildWhereStmt(filter)))) {
            try (ResultSet rs = pstm.executeQuery()) {
                // Iterates all results
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
            }
        } catch (DBException | SQLException e) {
            throw new DBException(e);
        }
        return list;
    }

    public static void main(String[] args) throws DBException {
        try (Connection conn = ConnectionFactory.query()) {
            GenericDAO<Test> dao = new GenericDAO<>(conn, Test.class);
            Filter filter = new FilterImpl();
            filter.add("keyy", FilterCondition.EQUAL, "2");
//            filter.add("value", FilterCondition.EQUAL, "3");
            System.out.println(dao.buildSelectStmt().concat(dao.buildWhereStmt(filter)));
            dao.select(filter).forEach((c) -> {
                System.out.println(c);
            });

        }
    }

    private static class Test {

        public Test() {
        }

        public int keyy;
        public String valuee;

        public int getKeyy() {
            return keyy;
        }

        public void setKeyy(int keyy) {
            this.keyy = keyy;
        }

        public String getValuee() {
            return valuee;
        }

        public void setValuee(String valuee) {
            this.valuee = valuee;
        }

        @Override
        public String toString() {
            return "Test{" + "keyy=" + keyy + ", valuee=" + valuee + '}';
        }

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

    /**
     * UPDATE a register on the database and returns true if it's modified
     *
     * @param bean
     * @return List
     * @throws DBException
     */
    @Override
    public boolean update(B bean) throws DBException {
        try (PreparedStatement pstm = conn.prepareStmt(buildUpdateStmt())) {
            Field[] fields = beanClass.getDeclaredFields();
            try {
                int i = 1;
                for (Field field : fields) {
                    Object value = getGetter(field).invoke(bean);
                    pstm.setObject(i++, value);
                }
                // Primary key
                Object value = getGetter(fields[0]).invoke(bean);
                pstm.setObject(i, value);
            } catch (ReflectiveOperationException e) {
                throw new DBException(e);
            }
            // Executes the update
            int executeUpdate = pstm.executeUpdate();
            return executeUpdate > 0;
        } catch (DBException | SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * DELETE a register on the database and returns true if it's deleted
     *
     * @param bean
     * @return List
     * @throws DBException
     */
    @Override
    public boolean delete(B bean) throws DBException {
        try (PreparedStatement pstm = conn.prepareStmt(buildDeleteStmt())) {
            Field[] fields = beanClass.getDeclaredFields();
            try {
                // Primary key
                Object value = getGetter(fields[0]).invoke(bean);
                pstm.setObject(1, value);
            } catch (ReflectiveOperationException e) {
                throw new DBException(e);
            }
            // Executes the update
            int executeUpdate = pstm.executeUpdate();
            return executeUpdate > 0;
        } catch (DBException | SQLException e) {
            throw new DBException(e);
        }
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
     * Builds the Insert Statement
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
     * Builds the Update Statement
     *
     * @return String
     */
    public String buildUpdateStmt() {
        StringBuilder sb = new StringBuilder(256).
                append("UPDATE ").
                append(beanClass.getSimpleName()).
                append(" SET ");
        // For all declared fields
        Field[] fields = beanClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            sb.append(f.getName()).append(" = ?");
            if (i < fields.length - 1) {
                sb.append(", ");
            }
        }
        // First field is the primary key
        return sb.append(" WHERE ").
                append(String.format(" %s ", fields[0].getName())).
                append(" = ?").
                toString();
    }

    /**
     * Builds the Delete Statement
     *
     * @return String
     */
    public String buildDeleteStmt() {
        StringBuilder sb = new StringBuilder(256).
                append("DELETE FROM ").
                append(beanClass.getSimpleName());
        // First field is the primary key
        Field[] fields = beanClass.getDeclaredFields();
        return sb.append(" WHERE ").
                append(String.format(" %s ", fields[0].getName())).
                append(" = ?").
                toString();
    }

    /**
     * Builds the Where Statement based on a filter
     *
     * @return String
     */
    private String buildWhereStmt(Filter filter) {
        StringBuilder sb = new StringBuilder(256);
        if (!filter.isEmpty()) {
            sb.append(" WHERE ");
        }

        List<FilterItem> get = filter.get();
        int len = get.size();
        for (int i = 0; i < len; i++) {
            FilterItem item = get.get(i);
            sb.append(item.getField()).append(item.getCondition()).append(item.getValue());
            if (i < len - 1) {
                sb.append(" AND ");
            }
        }

        return sb.toString();
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
