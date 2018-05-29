/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.utils;

import br.jpe.core.encrypt.Base64;
import br.jpe.core.encrypt.Criptography;
import br.jpe.core.encrypt.Md5;
import br.jpe.core.encrypt.Rot13;

/**
 * A classe to help with encryptation
 *
 * @author joaovperin
 */
public final class CryptUtils {

    /**
     * Returns a new Rot13 cryptography class
     *
     * @return Criptography
     */
    public static Criptography rot13() {
        return new Rot13();
    }

    /**
     * Returns a new Md5 cryptography class
     *
     * @return Criptography
     */
    public static Criptography md5() {
        return new Md5();
    }

    /**
     * Returns a new Base64 cryptography class
     *
     * @return Criptography
     */
    public static Criptography base64() {
        return new Base64();
    }

}
