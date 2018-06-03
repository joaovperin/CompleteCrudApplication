/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An HttpChain Node
 *
 * @author joaovperin
 */
public interface HttpChainNode {

    /**
     * Proccess the node
     *
     * @param request
     * @param response
     */
    public void proccess(HttpServletRequest request, HttpServletResponse response);

}
