/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Http Chain
 *
 * @author joaovperin
 */
public abstract class HttpChain {

    /** List of nodes */
    private final List<HttpChainNode> nodes = new ArrayList<>();
    /** Iterator for the nodes */
    private Iterator<HttpChainNode> iterator;

    /**
     * Starts the chain
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public final void start(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Start of the chain
        if (iterator == null) {
            iterator = nodes.iterator();
        }
        // Procces with the next node or finish the chain
        if (iterator.hasNext()) {
            iterator.next().proccess(request, response);
        } else {
            finish(request, response);
        }
    }

    /**
     * To be called when the chain finishes
     *
     * @param request
     * @param response
     * @throws java.lang.Exception
     */
    public void finish(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }

    /**
     * Adds a node to the list
     *
     * @param node
     */
    public final void add(HttpChainNode node) {
        if (iterator != null) {
            throw new IllegalStateException("Cannot add Controllers after calling doFilter");
        }
        nodes.add(node);
    }

}
