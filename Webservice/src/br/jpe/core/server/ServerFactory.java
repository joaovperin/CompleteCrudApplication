/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.server;

import br.jpe.core.Options;
import br.jpe.core.OptionsParser;

/**
 *
 * @author programacao
 */
public final class ServerFactory {

    private final Options options;

    public ServerFactory(String[] args) {
        this.options = OptionsParser.parse(getArgsOrDefaults(args));
    }

    public final Server create() {
        TomcatServer sv = new TomcatServer();
        sv.setOptions(options);
        return sv;
    }

    public final String[] getArgsOrDefaults(String[] args) {
        if ((args != null && args.length > 0)) {
            return args;
        }
        // Return defaults
        return new String[]{"-port=8085"};
    }

}
