/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.utils;

/**
 * A Helper to work with Texts/Strings
 *
 * @author joaovperin
 */
public final class TextX {

    /**
     * Capitalizes a text
     *
     * @param input
     * @return String
     */
    public static String capitalize(String input) {
        return input.substring(0, 1).toUpperCase().concat(input.substring(1, input.length()));
    }

}
