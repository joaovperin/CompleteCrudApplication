/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.config;

import java.io.PrintStream;

/**
 * An interface for Options
 *
 * @author joaovperin
 */
public interface Options {

    /**
     * Stores an option
     *
     * @param key
     * @param value
     */
    public void put(String key, String value);

    /**
     * Retrieve an option value or it's default value if not present
     *
     * @param key
     * @param defaultValue
     * @return String
     */
    public String get(String key, String defaultValue);

    /**
     * Prints all options
     *
     * @param printStream
     */
    public void printAll(PrintStream printStream);

}
