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
public interface Server {

    public void setOptions(Options options);

    public void configure();

    public void run();

    public void stop();

}
