/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    private final Map<Pattern, Filter> filters = new LinkedHashMap<>();

    /**
     * Initializes the filter
     *
     * @param cfg
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig cfg) throws ServletException {
        System.out.printf("*** Initializing filter Version %s\n", VERSION);
        Filter f1 = new DummyFilter();
        f1.init(cfg);
        filters.put(new Pattern("/foo/*"), f1);
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
        MainFilterChain godChain = new MainFilterChain(chain);
        filters.entrySet().stream().filter((entry) -> {
            return entry.getKey().matches(path);
        }).forEachOrdered((entry) -> {
            godChain.addFilter(entry.getValue());
        });
        // Executes the Chain
        godChain.doFilter(req, res);
    }

    /**
     * Called on the filter destroy
     */
    @Override
    public void destroy() {
        System.out.println("*** Destroy.");
    }

    /**
     * URL Pattern
     */
    private class Pattern {

        /** Position of the URL */
        private final int position;
        /** Full URL */
        private final String url;

        /**
         * Constructor
         *
         * @param url
         */
        public Pattern(String url) {
            this.url = url.replaceAll("/?\\*", "");
            this.position = url.startsWith("*") ? 1
                    : url.endsWith("*") ? -1
                    : 0;
        }

        /**
         * Returns true if the URL path matches with the specified url
         *
         * @param path
         * @return boolean
         */
        public boolean matches(String path) {
            return (position == -1) ? path.startsWith(url)
                    : (position == 1) ? path.endsWith(url)
                            : path.equals(url);
        }

    }

    /**
     * A Filter chain for the Main Filter
     */
    public class MainFilterChain implements FilterChain {

        /** Filter Chain */
        private final FilterChain chain;
        /** List of filters */
        private final List<Filter> filters = new ArrayList<>();
        /** Iterator for the filters */
        private Iterator<Filter> iterator;

        /**
         * Constructor
         *
         * @param chain
         */
        public MainFilterChain(FilterChain chain) {
            this.chain = chain;
        }

        /**
         * Do the filter logic
         *
         * @param request
         * @param response
         * @throws IOException
         * @throws ServletException
         */
        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            // Start of the chain
            if (iterator == null) {
                iterator = filters.iterator();
            }
            // Procces with the next filter or continue with the main filter chain
            if (iterator.hasNext()) {
                iterator.next().doFilter(request, response, this);
            } else {
                chain.doFilter(request, response);
            }
        }

        /**
         * Adds a filter to the list
         *
         * @param filter
         */
        public void addFilter(Filter filter) {
            if (iterator != null) {
                throw new IllegalStateException("Cannot add filters after calling doFilter");
            }
            filters.add(filter);
        }

    }

}
