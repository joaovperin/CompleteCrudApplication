/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.encrypt;

import java.nio.charset.Charset;

/**
 * Base64 Encrypt/decrypt
 *
 * @author joaovperin
 */
public final class Base64 implements Criptography {

    @Override
    public String encrypt(String input) {
        byte[] bytes = input.getBytes(Charset.defaultCharset());
        return java.util.Base64.getEncoder().encodeToString(bytes);
    }

    @Override
    public String decrypt(String input) {
        byte[] decoded = java.util.Base64.getDecoder().decode(input);
        return new String(decoded, Charset.defaultCharset());
    }

    @Override
    public String encrypt(String input, String key) {
        throw new UnsupportedOperationException("Base64 doesn't uses a key to encode.");
    }

    @Override
    public String decrypt(String input, String key) {
        throw new UnsupportedOperationException("Base64 doesn't uses a key to decode.");
    }

}
