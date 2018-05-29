/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.server;

import br.jpe.core.Options;

/**
 * Abstraction of a server
 *
 * @author joaovperin
 */
public abstract class AbstractServer implements Server {

    /** Server options */
    protected Options options;

    /**
     * Default Constructor
     */
    public AbstractServer() {
        this.options = new Options();
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    /**
     * Set the server options
     *
     * @param options
     */
    @Override
    public void setOptions(Options options) {
        this.options = options;
    }

}
