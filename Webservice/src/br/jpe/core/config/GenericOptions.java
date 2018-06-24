/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.config;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A Generic options object
 *
 * @author joaovperin
 */
public class GenericOptions implements Options {

    /** Map with the options */
    private final Map<String, String> options;
    /** Character separator to print options */
    private static final String SEPARATOR = "=";

    /**
     * Default constructor
     */
    public GenericOptions() {
        this(new HashMap<>());
    }

    /**
     * Constructor
     *
     * @param options
     */
    private GenericOptions(Map<String, String> options) {
        this.options = options;
    }

    /**
     * Puts an object in the options map (if not exists)
     *
     * @param key
     * @param value
     */
    @Override
    public final void put(String key, String value) {
        String entry = options.get(key);
        if (entry == null) {
            options.put(key, value);
        }
    }

    /**
     * Returns an option from the map
     *
     * @param key
     * @param defaultValue
     * @return String
     */
    @Override
    public final String get(String key, String defaultValue) {
        String value = options.get(key);
        if (value != null) {
            String trim = value.trim();
            if (!trim.isEmpty() && !trim.equals("0")) {
                return value;
            }
        }
        return defaultValue;
    }

    /**
     * Prints all the options
     *
     * @param printStream
     */
    @Override
    public final void printAll(PrintStream printStream) {
        options.entrySet().forEach((set) -> {
            printStream.println(new StringBuilder().
                    append(set.getKey()).
                    append(SEPARATOR).
                    append(set.getValue()).
                    append('\n'));
        });
    }

    /**
     * Copies the object
     *
     * @return GenericOptios
     */
    public GenericOptions copy() {
        Map<String, String> newOptions = new HashMap<>();
        options.entrySet().forEach((set) -> newOptions.put(set.getKey(), set.getValue()));
        return new GenericOptions(newOptions);
    }

}
