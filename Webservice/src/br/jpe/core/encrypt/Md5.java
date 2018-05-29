/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.encrypt;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5 Hashing Encrypt
 *
 * @author joaovperin
 */
public final class Md5 implements Criptography {

    @Override
    public String encrypt(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            return new BigInteger(1, md.digest(input.getBytes())).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Failed to load MD5", ex);
        }
    }

    @Override
    public String encrypt(String input, String key) {
        throw new UnsupportedOperationException("MD5 doesn't uses a key to encrypt.");
    }

    @Override
    public String decrypt(String input) {
        throw new UnsupportedOperationException("MD5 cannot be decrypted.");
    }

    @Override
    public String decrypt(String input, String key) {
        throw new UnsupportedOperationException("MD5 cannot be decrypted, and if it could, it would not use a key.");
    }

}
