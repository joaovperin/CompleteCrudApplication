/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core;

import br.jpe.core.server.Server;
import br.jpe.core.server.ServerFactory;

/**
 *
 * @author programacao
 */
public class Main {

    public static void main(String[] args) {
        Server server = new ServerFactory(args).create();
        server.configure();
        server.run();
    }

}
