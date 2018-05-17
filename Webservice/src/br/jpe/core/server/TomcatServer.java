/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.server;

/**
 *
 * @author programacao
 */
public class TomcatServer extends AbstractServer {

    @Override
    public void run() {
        System.out.println("*** Server starting...");
    }

    @Override
    public void stop() {
        System.out.println("*** Server stopping...");
    }

    @Override
    public void configure() {
        System.out.println("*** Server configuring...");
        options.printAll(System.out);
    }

}
