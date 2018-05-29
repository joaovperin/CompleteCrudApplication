/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.encrypt;

/**
 * An interface to group all cryptography classes
 *
 * @author joaovperin
 */
public interface Criptography {

    /**
     * Encrypts an Input
     *
     * @param input
     * @return String
     */
    public String encrypt(String input);

    /**
     * Encrypts an Input based on an cryptograph key
     *
     * @param input
     * @param key
     * @return String
     */
    public String encrypt(String input, String key);

    /**
     * Decrypts an Input
     *
     * @param input
     * @return String
     */
    public String decrypt(String input);

    /**
     * Decrypts an Input based on an cryptograph key
     *
     * @param input
     * @param key
     * @return String
     */
    public String decrypt(String input, String key);

}
