/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.utils;

import java.io.File;
import java.io.PrintStream;

/**
 * A class with utilities to deal with files
 *
 * @author joaovperin
 */
public class FileX {

    /** Default line separator */
    private static final String SEP = System.lineSeparator();

    /**
     * Creates a directory if not exists
     *
     * @param fileName
     */
    public static void createDirIfNotExists(String fileName) {
        createDirIfNotExists(fileName, null);
    }

    /**
     * Creates a directory if not exists
     *
     * @param fileName
     * @param out
     */
    public static void createDirIfNotExists(String fileName, PrintStream out) {
        createDirIfNotExists(new File(fileName), out);
    }

    /**
     * Creates a directory if not exists
     *
     * @param file
     */
    public static void createDirIfNotExists(File file) {
        createDirIfNotExists(file, null);
    }

    /**
     * Creates a directory if not exists
     *
     * @param file
     * @param printStream
     */
    public static void createDirIfNotExists(File file, PrintStream printStream) {
        if (!file.exists() && file.isDirectory()) {
            if (printStream != null) {
                printStream.println("*** Creating directory ".concat(file.getName()).concat(" ..."));
            }
            file.mkdir();
        }
    }

    /**
     * Returns line separator of the system
     *
     * @return String
     */
    public static String getLineSeparator() {
        return SEP;
    }

}
