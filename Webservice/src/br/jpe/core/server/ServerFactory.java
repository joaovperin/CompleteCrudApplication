/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.server;

import br.jpe.core.server.impl.TomcatServer;
import br.jpe.core.config.OptionsParser;

/**
 * A Factory to help with server instantiation
 *
 * @author joaovperin
 */
public final class ServerFactory {

    /** Server options */
    private final ServerOptions options;

    /**
     * Constructor that builds the options using command line arguments
     *
     * @param commandLineArgs
     */
    public ServerFactory(String[] commandLineArgs) {
        this.options = (ServerOptions) OptionsParser.parse(getArgsOrDefaults(commandLineArgs), new ServerOptions());
    }

    /**
     * Creates a new Server
     *
     * @return Server
     */
    public final Server create() {
        TomcatServer sv = new TomcatServer();
        sv.setOptions(options);
        return sv;
    }

    /**
     * Returns the args passed and provides default values for missing args
     *
     * @param commandLineArgs
     * @return String[] Command line args
     */
    public final String[] getArgsOrDefaults(String[] commandLineArgs) {
        if ((commandLineArgs != null && commandLineArgs.length > 0)) {
            return commandLineArgs;
        }
        // Return defaults
        return new String[]{"-port=8085"};
    }

}
