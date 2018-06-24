/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.server;

/**
 * Abstraction of a server
 *
 * @author joaovperin
 */
public abstract class AbstractServer implements Server {

    /** Server options */
    protected ServerOptions options;

    /**
     * Default Constructor
     */
    public AbstractServer() {
        this.options = new ServerOptions();
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    /**
     * Set the server options
     *
     * @param options
     */
    @Override
    public void setOptions(ServerOptions options) {
        this.options = options;
    }

    /**
     * Returns the server options
     *
     * @return
     */
    @Override
    public ServerOptions getOptions() {
        return options.copy();
    }

}
