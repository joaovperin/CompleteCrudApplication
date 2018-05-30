/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.utils;

import java.util.Properties;

/**
 * A class to help with System things
 *
 * @author joaovperin
 */
public class SystemX {

    /**
     * Returns operation system
     *
     * @return Os
     */
    public static String getOsName() {
        return Os.get().name;
    }

    /**
     * Returns the system properties
     *
     * @return Properties
     */
    public static Properties getProperties() {
        return System.getProperties();
    }

}
