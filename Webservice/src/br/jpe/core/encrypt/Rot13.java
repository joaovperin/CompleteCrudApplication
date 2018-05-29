/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.encrypt;

/**
 * Rot13 Encrypt/Decrypt
 *
 * @author joaovperin
 */
public final class Rot13 implements Criptography {

    /** Constant to the number of rotations */
    private static final int ROT_13 = 13;

    @Override
    public String encrypt(String input) {
        return rotate(input);
    }

    @Override
    public String decrypt(String input) {
        return rotate(input);
    }

    @Override
    public String encrypt(String input, String key) {
        throw new UnsupportedOperationException("ROT13 Doesn't uses a key to encrypt.");
    }

    @Override
    public String decrypt(String input, String key) {
        throw new UnsupportedOperationException("ROT13 Doesn't uses a key to decrypt.");
    }

    /**
     * Rotates the string characters
     *
     * @param input
     * @return String
     */
    private String rotate(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c >= 'a' && c <= 'm') {
                c += ROT_13;
            } else if (c >= 'A' && c <= 'M') {
                c += ROT_13;
            } else if (c >= 'n' && c <= 'z') {
                c -= ROT_13;
            } else if (c >= 'N' && c <= 'Z') {
                c -= ROT_13;
            }
            sb.append(c);
        }
        return sb.toString();
    }

}
