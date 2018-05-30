/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.utils;

/**
 * Operational System Information
 *
 * @author joaovperin
 */
public class Os {

    /** OS Name */
    String name;

    /**
     * Returns true if running on Windows OS
     *
     * @return boolean
     */
    public boolean isWindows() {
        return name.equals("Windows");
    }

    /**
     * Returns true if running on Linux
     *
     * @return boolean
     */
    public boolean isLinux() {
        return name.equals("Linux");
    }

    /**
     * Gatters info and returns a new instance of this class
     *
     * @return Os
     */
    public static Os get() {
        Os self = new Os();
        self.name = System.getProperty("os.name");
        return self;
    }

}
