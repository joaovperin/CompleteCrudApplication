/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import br.jpe.core.database.connection.Connection;

/**
 * A Factory utility to create DAO Objects
 *
 * @author joaovperin
 */
public final class DAOFactory {

    /** DAO Suffix */
    private static final String SUFFIX = "DAO";

    /**
     * Instantiate a new Data Access Object for the bean class
     *
     * @param <B>
     * @param bean
     * @param conn
     * @return DataAccessObject
     */
    public static final <B> DataAccessObject<B> create(Class<B> bean, Connection conn) {
        try {
            String pkgName = DAOFactory.class.getPackage().getName();
            Class<B> daoClass = (Class<B>) Class.forName(pkgName.concat(bean.getSimpleName()).concat(SUFFIX));
            return (DataAccessObject<B>) daoClass.getConstructor(Connection.class).newInstance(conn);
        } catch (IllegalArgumentException | SecurityException | ReflectiveOperationException e) {
            return new GenericDAO<>(conn, bean);
        }
    }

}
