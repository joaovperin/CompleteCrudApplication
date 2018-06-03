/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.servlet;

import br.jpe.controller.TestController;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Main Filter for the Servlets API
 *
 * @author joaovperin
 */
@WebFilter(urlPatterns = "/*", dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
public class MainFilter implements Filter {

    /** Filter Version */
    private static final String VERSION = "0.0.1-SNAPSHOT";

    /** Filters map */
    private final Map<Pattern, ControllerClass> controllers = new LinkedHashMap<>();

    /**
     * Initializes the filter
     *
     * @param cfg
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig cfg) throws ServletException {
        System.out.printf("*** Initializing filter Version %s\n", VERSION);
        // Controller class
        ControllerClass c = new TestController();
        controllers.put(c.getPattern(), c);
    }

    /**
     * The filter logic
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("*** DoFilter");
        // Ensure its running through a valid context
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("It MUST be running though an HttpServlet context.");
        }
        // HttpServlet casts
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;
        // Cut's the request path
        String path = req.getRequestURI().substring(req.getContextPath().length());
        // Creates a GodChain filter chain with all the matching filters
        MainFilterChain execChain = new MainFilterChain(chain);
        // All Controllers
        controllers.entrySet().stream().filter((entry) -> {
            return entry.getKey().matches(path);
        }).forEachOrdered((Map.Entry<Pattern, ControllerClass> entry) -> {
            // Get methods that match
            Stream stream = entry.getValue().getMethodsThatMatch(path).stream();
            stream.forEachOrdered(new Consumer<ControllerMethod>() {
                @Override
                public void accept(ControllerMethod m) {
                    execChain.add((HttpServletRequest rq1, HttpServletResponse res1) -> {
                        m.exec(rq1, res1);
                    });
                }
            });
        });
        // Executes the Chain
        try {
            execChain.start(req, res);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Called on the filter destroy
     */
    @Override
    public void destroy() {
        System.out.println("*** Destroy.");
    }

    /**
     * A Filter chain for the Main Filter
     */
    public class MainFilterChain extends HttpChain {

        /** Filter Chain */
        private final FilterChain chain;

        /**
         * Constructor
         *
         * @param chain
         */
        public MainFilterChain(FilterChain chain) {
            this.chain = chain;
        }

        /**
         * Called on finish
         *
         * @param request
         * @param response
         * @throws IOException
         * @throws ServletException
         */
        @Override
        public void finish(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            chain.doFilter(request, response);
        }

    }

}
