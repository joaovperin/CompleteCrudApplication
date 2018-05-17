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
 *
 * @author programacao
 */
public final class Options {

    private final Map<String, String> options;
    private static final String SEPARATOR = "=";

    public Options() {
        this.options = new HashMap<>();
    }

    public void put(String key, String value) {
        String entry = options.get(key);
        if (entry == null) {
            options.put(key, value);
        }
    }

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

    public final void printAll(PrintStream out) {
        options.entrySet().forEach((set) -> {
            out.println(new StringBuilder().
                    append(set.getKey()).
                    append(SEPARATOR).
                    append(set.getValue()).
                    append('\n'));
        });
    }

}
