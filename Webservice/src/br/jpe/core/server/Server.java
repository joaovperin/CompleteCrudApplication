/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.server;

/**
 * A Server interface
 *
 * @author joaovperin
 */
public interface Server {

    /**
     * Set/store the server options
     *
     * @param options
     */
    public void setOptions(ServerOptions options);

    /**
     * Users the stored options (or the defaults) to configure the server
     */
    public void configure();

    /**
     * Runs the server on the configured options and starts listening to the
     * port
     */
    public void run();

    /**
     * Stops the server to run and do all the cleanup stuff
     */
    public void stop();

}
