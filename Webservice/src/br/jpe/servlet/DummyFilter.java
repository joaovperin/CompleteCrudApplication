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
 * Dummy Filter
 *
 * @author joaovperin
 */
public class DummyFilter implements Filter {

    @Override
    public void init(FilterConfig fc) throws ServletException {
        System.out.println("*** dummy init");
    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        System.out.println("*** dummy do filter");
    }

    @Override
    public void destroy() {
        System.out.println("*** dummy do destroy");
    }

}
