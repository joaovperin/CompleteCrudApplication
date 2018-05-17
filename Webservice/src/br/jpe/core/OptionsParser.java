/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core;

import java.util.Arrays;

/**
 *
 * @author programacao
 */
public class OptionsParser {

    private static final String BOOLEAN_VALUE_PREFIX = "--";
    private static final String KEY_VALUE_PREFIX = "-";
    private static final String SEPARATOR = "=";

    public static Options parse(String[] args) {
        Options options = new Options();
        Arrays.asList(args).stream().filter((str) -> {
            return isValidArgument(str);
        }).map((a) -> {
            return parseCommandLineArg(a);
        }).forEach((pair) -> options.put(pair.key, pair.value));
        return options;
    }

    private static boolean isValidArgument(String arg) {
        if (arg == null) {
            return false;
        }
        // Checks if its a boolean argument
        int indexOfBoolPrefix = arg.indexOf(BOOLEAN_VALUE_PREFIX);
        if (indexOfBoolPrefix == 0 && !arg.contains(SEPARATOR)) {
            return true;
        }
        // Checks if its a named key/value argument
        int indexOfKeyValuePrefix = arg.indexOf(KEY_VALUE_PREFIX);
        if (indexOfKeyValuePrefix == 0) {
            return arg.contains(SEPARATOR) && arg.length() >= 4;
        }
        return false;
    }

    private static KeyValuePair parseCommandLineArg(String arg) {
        int idxOfSeparatorCharacter = arg.indexOf(SEPARATOR);
        // Boolean argument is set to true if present
        if (idxOfSeparatorCharacter == -1) {
            String key = arg.substring(BOOLEAN_VALUE_PREFIX.length(), arg.length());
            return new KeyValuePair(key, String.valueOf(Boolean.TRUE.booleanValue()));
        }
        // Default Key/Value pair
        String key = arg.substring(KEY_VALUE_PREFIX.length(), idxOfSeparatorCharacter);
        String value = arg.substring(idxOfSeparatorCharacter + KEY_VALUE_PREFIX.length(), arg.length());
        return new KeyValuePair(key, value);
    }

    private static class KeyValuePair {

        public KeyValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        private String key;
        private String value;
    }

}
