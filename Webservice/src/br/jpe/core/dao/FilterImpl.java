/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a filter
 *
 * @joaovperin
 */
public class FilterImpl implements Filter {

    List<FilterItem> list = new ArrayList<>();

    @Override
    public void begin() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void end() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void add(String field, FilterCondition condition, String value) {
        list.add(new FilterItem(field, condition, value));
    }

    @Override
    public List<FilterItem> get() {
        // TODO: Returns a copy.
        return list;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

}
