/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.server;

import br.jpe.core.Options;

/**
 *
 * @author programacao
 */
public abstract class AbstractServer implements Server {

    protected Options options;

    public AbstractServer() {
        options = new Options();
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    @Override
    public void setOptions(Options options) {
        this.options = options;
    }

}
