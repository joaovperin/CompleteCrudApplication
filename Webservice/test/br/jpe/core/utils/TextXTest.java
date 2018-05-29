/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.utils;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author programacao
 */
public class TextXTest {

    public TextXTest() {
    }

    /**
     * Test of capitalize method, of class TextX.
     */
    @Test
    public void testCapitalize() {
        String input = "potato";
        assertEquals("Potato", TextX.capitalize(input));
    }

    /**
     * Test of uncapitalize method, of class TextX.
     */
    @Test
    public void testUncapitalize() {
        String input = "Potato";
        assertEquals("potato", TextX.uncapitalize(input));
    }

    /**
     * Test of toCamelCase method, of class TextX.
     */
    @Test
    public void testToCamelCase() {
        String input = "foo_bar";
        assertEquals("fooBar", TextX.toCamelCase(input, false));

        input = "foo_bar";
        assertEquals("FooBar", TextX.toCamelCase(input, true));
    }

    /**
     * Test of toUnderScore method, of class TextX.
     */
    @Test
    public void testToUnderScore() {
        String input = "fooBar";
        assertEquals("foo_Bar", TextX.toUnderScore(input));

        input = "FooBar";
        assertEquals("Foo_Bar", TextX.toUnderScore(input));
    }

    /**
     * Test of removeFirst method, of class TextX.
     */
    @Test
    public void testRemoveFirst() {
        String input = "potato";
        assertEquals("potato", TextX.removeFirst("\\(", input));

        input = "((potato";
        assertEquals("(potato", TextX.removeFirst("\\(", input));
    }

    /**
     * Test of removeLast method, of class TextX.
     */
    @Test
    public void testRemoveLast() {
        String input = "potato";
        assertEquals("potato", TextX.removeLast("\\)", input));

        input = "potato))x";
        assertEquals("potato)x", TextX.removeLast("\\)", input));
    }

    /**
     * Test of removeCharAt method, of class TextX.
     */
    @Test
    public void testRemoveCharAt() {
        String input = "potato";
        assertEquals("potto", TextX.removeCharAt(input, 3));

        input = "potato";
        assertEquals("potato", TextX.removeCharAt(input, 20));
    }

    /**
     * Test of removeOuterParenthesis method, of class TextX.
     */
    @Test
    public void testRemoveOuterParenthesis() {
        String input = "potato";
        assertEquals("potato", TextX.removeOuterParenthesis(input));

        input = "((potato))";
        assertEquals("(potato)", TextX.removeOuterParenthesis(input));
    }

}
