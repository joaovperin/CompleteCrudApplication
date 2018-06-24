/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import br.jpe.core.database.DBException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import br.jpe.core.utils.ReflectionX;
import java.sql.PreparedStatement;
import java.sql.Statement;
import br.jpe.core.database.sql.connection.SQLConnection;

/**
 * A Generic Data Access Object
 *
 * @author joaovperin
 * @param <B> Bean class
 */
public class GenericDAO<B> implements DataAccessObject<B> {

    /** Bean Class */
    private final Class<B> beanClass;
    /** SQL Builder object */
    private final SQLBuilder sql;
    /** Connection to the database */
    protected final SQLConnection conn;

    /**
     * Constructor that receives the connection
     *
     * @param conn
     * @param beanClass
     */
    public GenericDAO(SQLConnection conn, Class<B> beanClass) {
        this.beanClass = beanClass;
        this.sql = new SQLBuilder(beanClass);
        this.conn = Objects.requireNonNull(conn, "A valid connection is needed.");
    }

    /**
     * Executes a SELECT statement on the database and returns a list of beans
     *
     * @return List
     * @throws DBException
     */
    @Override
    public List<B> findAll() throws DBException {
        List<B> list = new ArrayList<>();
        try (Statement stm = conn.createStmt()) {
            try (ResultSet rs = stm.executeQuery(sql.buildSelectStmt())) {
                while (rs.next()) {
                    try {
                        B bean = ReflectionX.instantiate(beanClass);
                        int i = 1;
                        for (Field field : beanClass.getDeclaredFields()) {
                            Object value = ReflectionX.getterResultSetInt(field).invoke(rs, i++);
                            ReflectionX.setter(beanClass, field).invoke(bean, value);
                        }
                        list.add(bean);
                    } catch (ReflectiveOperationException e) {
                        throw new DBException(e);
                    }
                }
            } catch (SQLException e) {
                throw new DBException(e);
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return list;
    }

    /**
     * Executes a SELECT statement on the database and returns a list of beans
     * based on a filter
     *
     * @param filter
     * @return List
     * @throws DBException
     */
    @Override
    public List<B> findAll(Filter filter) throws DBException {
        List<B> list = new ArrayList<>();
        try (PreparedStatement pstm = conn.prepareStmt(sql.buildSelectStmt().concat(sql.buildWhereStmt(filter)))) {
            try (ResultSet rs = pstm.executeQuery()) {
                // Iterates all results
                while (rs.next()) {
                    try {
                        B bean = ReflectionX.instantiate(beanClass);
                        int i = 1;
                        for (Field field : beanClass.getDeclaredFields()) {
                            Object value = ReflectionX.getterResultSetInt(field).invoke(rs, i++);
                            ReflectionX.setter(beanClass, field).invoke(bean, value);
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

    /**
     * Selects ONE register (or null)
     *
     * @param filter
     * @return B
     * @throws DBException
     */
    @Override
    public B findOne(Filter filter) throws DBException {
        // Adds a LIMIT clause to the filter
        Filter newFilter = new GenericFilter(filter);
        newFilter.addLimit(1);
        List<B> list = findAll(newFilter);
        // If no record fits the filter, returns null
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
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
        try (PreparedStatement pstm = conn.prepareStmt(sql.buildInsertStmt())) {
            Field[] fields = beanClass.getDeclaredFields();
            try {
                int i = 1;
                for (Field field : fields) {
                    Object value = ReflectionX.getter(beanClass, field).invoke(bean);
                    pstm.setObject(i++, value);
                }
            } catch (ReflectiveOperationException e) {
                throw new DBException(e);
            }
            // Executes the update and retrieve generated keys (if auto increment)
            int executeUpdate = pstm.executeUpdate();
            try (ResultSet rs = pstm.getGeneratedKeys()) {
                int i = 0;
                while (rs.next()) {
                    Field field = fields[i++];
                    Method getter = ReflectionX.getterResultSetInt(field);
                    ReflectionX.setter(beanClass, field).invoke(bean, getter.invoke(rs, i));
                }
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
        try (PreparedStatement pstm = conn.prepareStmt(sql.buildUpdateStmt().concat(sql.buildWhereStmt()))) {
            // Copy the array of fields ignoring the PK (first field)
            Field[] fields = beanClass.getDeclaredFields();
            // Executes the update set logic
            try {
                int i = 1;
                for (int j = 1; j < fields.length; j++) {
                    Field field = fields[j];
                    Object value = ReflectionX.getter(beanClass, field).invoke(bean);
                    pstm.setObject(i++, value);
                }
                // Primary key
                Object value = ReflectionX.getter(beanClass, fields[0]).invoke(bean);
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
     * UPDATE all registers on the database and returns how much records were
     * updated
     *
     * @param bean
     * @return long
     * @throws DBException
     */
    @Override
    public long updateAll(B bean) throws DBException {
        return updateAll(bean, new GenericFilter());
    }

    /**
     * UPDATE all registers on the database and returns how much records were
     * updated. This works based on a filter
     *
     * @param bean
     * @param filter
     * @return long
     * @throws DBException
     */
    @Override
    public long updateAll(B bean, Filter filter) throws DBException {
        try (PreparedStatement pstm = conn.prepareStmt(sql.buildUpdateStmt().concat(sql.buildWhereStmt(filter)))) {
            // Copy the array of fields ignoring the PK (first field)
            Field[] fields = beanClass.getDeclaredFields();
            // Executes the update set logic
            try {
                int i = 1;
                for (int j = 1; j < fields.length; j++) {
                    Field field = fields[j];
                    Object value = ReflectionX.getter(beanClass, field).invoke(bean);
                    pstm.setObject(i++, value);
                }
            } catch (ReflectiveOperationException e) {
                throw new DBException(e);
            }
            // Executes the update
            int executeUpdate = pstm.executeUpdate();
            return executeUpdate;
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
        try (PreparedStatement pstm = conn.prepareStmt(sql.buildDeleteStmt().concat(sql.buildWhereStmt()))) {
            Field[] fields = beanClass.getDeclaredFields();
            try {
                // Primary key
                Object value = ReflectionX.getter(beanClass, fields[0]).invoke(bean);
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
     * DELETE registers on the database and returns how much records were
     * deleted
     *
     * @return long
     * @throws DBException
     */
    @Override
    public long deleteAll() throws DBException {
        return deleteAll(new GenericFilter());
    }

    /**
     * DELETE registers on the database and returns how much records were
     * deleted. This works based on a Filter
     *
     * @param filter
     * @return long
     * @throws DBException
     */
    @Override
    public long deleteAll(Filter filter) throws DBException {
        try (PreparedStatement pstm = conn.prepareStmt(sql.buildDeleteStmt().concat(sql.buildWhereStmt(filter)))) {
            // Executes the update
            int executeUpdate = pstm.executeUpdate();
            return executeUpdate;
        } catch (DBException | SQLException e) {
            throw new DBException(e);
        }
    }

}
