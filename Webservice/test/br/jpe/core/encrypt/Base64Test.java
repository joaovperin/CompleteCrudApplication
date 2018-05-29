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
 * Tests the Base64 Encrypting/Decrypting
 *
 * @author joaovperin
 */
public class Base64Test {

    /** Encriptor instance */
    private final Criptography instance = CryptUtils.base64();

    /**
     * Test of encrypt method, of class Base64.
     */
    @Test
    public void testEncrypt() {
        String input = "foobarhehe";
        assertEquals("Zm9vYmFyaGVoZQ==", instance.encrypt(input));
    }

    /**
     * Test of decrypt method, of class Base64.
     */
    @Test
    public void testDecrypt() {
        String input = "SlFOU0MtT0xLQ0ctVkNKUUgtUERKT0stRllaVko=";
        assertEquals("JQNSC-OLKCG-VCJQH-PDJOK-FYZVJ", instance.decrypt(input));
    }

}
