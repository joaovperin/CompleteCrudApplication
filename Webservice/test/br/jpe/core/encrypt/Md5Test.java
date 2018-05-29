/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.encrypt;

import br.jpe.core.utils.CryptUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the Md5 Encrypting
 *
 * @author joaovperin
 */
public class Md5Test {

    /** Encriptor instance */
    private final Criptography instance = CryptUtils.md5();

    /**
     * Test of encrypt method, of class Md5.
     */
    @Test
    public void testEncrypt() {
        String input = "123456";
        assertEquals("e10adc3949ba59abbe56e057f20f883e", instance.encrypt(input));
    }

}
