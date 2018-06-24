/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import br.jpe.core.database.DBException;
import java.util.List;

/**
 * An interface for DAOs
 *
 * @author joaovperin
 * @param <B> Bean class
 */
public interface DataAccessObject<B> {

    /**
     * Executes a SELECT statement on the database and returns a list of beans
     *
     * @return List
     * @throws DBException
     */
    public List<B> findAll() throws DBException;

    /**
     * Executes a SELECT statement on the database and returns a list of beans
     * based on a filter
     *
     * @param filter
     * @return List
     * @throws DBException
     */
    public List<B> findAll(Filter filter) throws DBException;

    /**
     * Executes a SELECT statement on the database and returns the first bean
     * matching the filter
     *
     * @param filter
     * @return List
     * @throws DBException
     */
    public B findOne(Filter filter) throws DBException;

    /**
     * INSERT a register on the database and returns true if it works
     *
     * @param bean
     * @return boolean
     * @throws DBException
     */
    public boolean insert(B bean) throws DBException;

    /**
     * UPDATE a register on the database and returns true if it's modified
     *
     * @param bean
     * @return boolean
     * @throws DBException
     */
    public boolean update(B bean) throws DBException;

    /**
     * UPDATE all registers on the database and returns how much records were
     * updated
     *
     * @param bean
     * @return long
     * @throws DBException
     */
    public long updateAll(B bean) throws DBException;

    /**
     * UPDATE all registers on the database and returns how much records were
     * updated. This works based on a filter
     *
     * @param bean
     * @param filter
     * @return long
     * @throws DBException
     */
    public long updateAll(B bean, Filter filter) throws DBException;

    /**
     * DELETE a register on the database and returns true if it's deleted
     *
     * @param bean
     * @return boolean
     * @throws DBException
     */
    public boolean delete(B bean) throws DBException;

    /**
     * DELETE all registers on the database and returns how much records were
     * deleted
     *
     * @return long
     * @throws DBException
     */
    public long deleteAll() throws DBException;

    /**
     * DELETE registers on the database and returns how much records were
     * deleted. This works based on a Filter
     *
     * @param filter
     * @return long
     * @throws DBException
     */
    public long deleteAll(Filter filter) throws DBException;

}
