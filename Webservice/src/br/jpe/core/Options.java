/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Options object
 *
 * @author joaovperin
 */
public final class Options {

    /** Map with the options */
    private final Map<String, String> options;
    /** Character separator to print options */
    private static final String SEPARATOR = "=";

    /**
     * Default constructor
     */
    public Options() {
        this.options = new HashMap<>();
    }

    /**
     * Puts an object in the options map (if not exists)
     *
     * @param key
     * @param value
     */
    public void put(String key, String value) {
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
    public String get(String key, String defaultValue) {
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
    public final void printAll(PrintStream printStream) {
        options.entrySet().forEach((set) -> {
            printStream.println(new StringBuilder().
                    append(set.getKey()).
                    append(SEPARATOR).
                    append(set.getValue()).
                    append('\n'));
        });
    }

}
