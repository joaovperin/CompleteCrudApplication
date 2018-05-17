/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author programacao
 */
public class OptionsParserTest {

    @Test
    public void testKeyValueArg() {
        Options options = OptionsParser.parse(new String[]{"-key=value"});
        assertEquals("value", options.get("key", null));
    }

    @Test
    public void testBooleanKeyArg() {
        Options options = OptionsParser.parse(new String[]{"-key"});
        assertEquals("true", options.get("key", null));
    }

    @Test
    public void testUnparsedArgs() {
        Options options = OptionsParser.parse(new String[]{"noprefix=value", "booltest"});
        // Boolean unparsed arg
        assertEquals(null, options.get("booltest", null));
        // NUll default value
        assertEquals(null, options.get("noprefix", null));
        // Any default value
        assertEquals("bar", options.get("foo", "bar"));
    }

}
