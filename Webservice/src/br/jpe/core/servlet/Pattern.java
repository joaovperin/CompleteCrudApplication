/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.servlet;

/**
 * URL Pattern
 *
 * @author joaovperin
 */
public class Pattern {

    /** Position of the URL */
    private final int position;
    /** Full URL */
    private final String url;

    /**
     * Constructor
     *
     * @param url
     */
    private Pattern(int position, String url) {
        this.position = position;
        this.url = url;
    }

    /**
     * Returns true if the URL path matches with the specified url
     *
     * @param path
     * @return boolean
     */
    public boolean matches(String path) {
        return (position == -1) ? path.startsWith(url)
                : (position == 1) ? path.endsWith(url)
                        : path.equals(url);
    }

    /**
     * Creates a subpattern
     *
     * @param url
     * @return Pattern
     */
    public final Pattern subPattern(String url) {
        return create(url);
    }

    /**
     * Creates a new instance of a pattern
     *
     * @param url
     * @return Pattern
     */
    public static Pattern create(String url) {
        int position = url.startsWith("*") ? 1 : url.endsWith("*") ? -1 : 0;
        return new Pattern(position, url.replaceAll("/?\\*", ""));
    }

}
