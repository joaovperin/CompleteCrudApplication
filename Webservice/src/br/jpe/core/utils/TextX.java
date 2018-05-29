/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Helper to work with Texts/Strings
 *
 * @author joaovperin
 */
public final class TextX {

    /** Pattern to convert Underscored strings to CamelCase */
    private static final Pattern PT_UNDERSCORE_TO_CAMELCASE = Pattern.compile("_(.)");
    /** Pattern to convert CamelCased strings to Underscored */
    private static final Pattern PT_CAMELCASE_TO_UNDERSCORE = Pattern.compile("([^_A-Z])([A-Z])");
    /** Pattern to detect parenthesis */
    private static final Pattern PT_PARENTHESIS = Pattern.compile("\\(.+\\)");

    /**
     * Capitalizes a text
     *
     * @param input
     * @return String
     */
    public static String capitalize(String input) {
        return input.substring(0, 1).toUpperCase().concat(input.substring(1, input.length()));
    }

    /**
     * Uncapitalizes a String
     *
     * @param input
     * @return String
     */
    public static String uncapitalize(String input) {
        return input.substring(0, 1).toLowerCase().concat(input.substring(1));
    }

    /**
     * Changes a text separated from UnderScores to CamelCase
     *
     * @param input Text
     * @param capFirst Capitalizes the first character?
     * @return String
     */
    public static String toCamelCase(String input, boolean capFirst) {
        Matcher m = PT_UNDERSCORE_TO_CAMELCASE.matcher(input);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, m.group(1).toUpperCase());
        }
        m.appendTail(sb);
        // Se deve capitalizar o primeiro
        if (capFirst) {
            return capitalize(sb.toString());
        }
        return sb.toString();
    }

    /**
     * Changes a text separated from CamelCase to UnderScores
     *
     * @param input Text
     * @return String
     */
    public static String toUnderScore(String input) {
        return PT_CAMELCASE_TO_UNDERSCORE.matcher(input).replaceAll("$1_$2");
    }

    /**
     * Removes the first occurrency of an expression in a text
     *
     * @param regex
     * @param input
     * @return String
     */
    public static String removeFirst(String regex, String input) {
        return input.replaceFirst(regex, "");
    }

    /**
     * Removes the last occurrency of an expression in a text
     *
     * @param regex
     * @param text
     * @return String
     */
    public static String removeLast(String regex, String text) {
        String pattern = "(?s)" + regex + "(?!.*?" + regex + ")";
        return text.replaceFirst(pattern, "");
    }

    /**
     * Removes char At index
     *
     * @param text
     * @param index
     * @return String
     */
    public static String removeCharAt(String text, int index) {
        if (index < 0 || index > text.length()) {
            return text;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(text.substring(0, index));
        sb.append(text.substring(index + 1, text.length()));
        return sb.toString();
    }

    /**
     * Removes the text outer parenthesis (not recursively)
     *
     * @param input
     * @return String
     */
    public static String removeOuterParenthesis(String input) {
        if (PT_PARENTHESIS.matcher(input).find()) {
            String temp = removeCharAt(input, input.length() - 1);
            return removeCharAt(temp, 0);
        }
        return input;
    }

}
