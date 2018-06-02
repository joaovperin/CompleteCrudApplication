/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.servlet;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Simple Main Filter of the Servlets API
 *
 * @author joaovperin
 */
public class MainFilter implements Filter {

    /** Filter Version */
    private static final String VERSION = "0.0.1-SNAPSHOT";

    /**
     * Initializes the filter
     *
     * @param cfg
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig cfg) throws ServletException {
        System.out.printf("*** Initializing filter Version %s\n", VERSION);
    }

    /**
     * The filter logic
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("*** DoFilter");
    }

    /**
     * Called on the filter destroy
     */
    @Override
    public void destroy() {
        System.out.println("*** Destroy.");
    }

}
