/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core;

import java.util.Arrays;

/**
 * A parser for parsing command line parameters into options
 *
 * @author joaovperin
 */
public class OptionsParser {

    /** Prefix for boolean parameters */
    private static final String BOOLEAN_VALUE_PREFIX = "--";
    /** Prefix for Key/Value parameters */
    private static final String KEY_VALUE_PREFIX = "-";
    /** Separator for Key/Value parameters */
    private static final String SEPARATOR = "=";

    /**
     * Parsers a String array into an Options object
     *
     * @param commandLineArgs
     * @return Options
     */
    public static Options parse(String[] commandLineArgs) {
        Options options = new Options();
        Arrays.asList(commandLineArgs).stream().filter((str) -> {
            return isValidArgument(str);
        }).map((a) -> {
            return parseCommandLineArg(a);
        }).forEach((pair) -> options.put(pair.key, pair.value));
        return options;
    }

    /**
     * Returns true if the argument is valid
     *
     * @param arg Command line argument
     * @return boolean
     */
    private static boolean isValidArgument(String arg) {
        if (arg == null || arg.trim().isEmpty()) {
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

    /**
     * Parses a valid command line arg into a Key/Value pair
     *
     * @param arg Command line argument
     * @return KeyValuePair
     */
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

    /**
     * A Helper class to hold Key/Value pairs
     */
    private static class KeyValuePair {

        /**
         * Constructor
         *
         * @param key
         * @param value
         */
        public KeyValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        private String key;
        private String value;
    }

}
