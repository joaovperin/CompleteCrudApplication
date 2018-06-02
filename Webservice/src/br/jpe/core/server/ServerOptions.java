/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.server;

import br.jpe.core.config.GenericOptions;

/**
 * Server Options
 *
 * @author joaovperin
 */
public class ServerOptions extends GenericOptions {

    /** Port attribute name */
    private static final String PORT = "port";
    /** Port attribute default value */
    private static final String PORT_DEF = "8085";

    /**
     * Gets the port
     * 
     * @return int
     */
    public int getPort() {
        return Integer.valueOf(get(PORT, PORT_DEF));
    }

}
