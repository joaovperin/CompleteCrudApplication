/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao;

/**
 * A class that represents a Filter Item
 *
 * @author joaovperin
 */
public class FilterItem {

    /** Name of the database field */
    private String field;
    /** Condition to be used */
    private FilterCondition condition;
    /** Value to filter */
    private String value;

}
