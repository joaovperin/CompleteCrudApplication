/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.servlet;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A class that simulates a controller
 *
 * @author joaovperin
 * @param <C>
 */
public abstract class ControllerClass<C> {

    /** Controller class */
    private final Class<? extends ControllerClass> c;
    /** List of the controller methods */
    private final List<ControllerMethod> methods;

    /**
     * Default constructor
     */
    public ControllerClass() {
        c = this.getClass();
        methods = new ArrayList<>();
        mapsAllControllerMethods();
    }

    /**
     * Returns the URL Pattern for this
     *
     * @return Pattern
     */
    public abstract Pattern getPattern();

    /**
     * Gets all controller methods that matches a uri path
     *
     * @param path
     * @return List
     */
    public final List<ControllerMethod> getMethodsThatMatch(String path) {
        return methods.stream().filter((ControllerMethod m) -> {
            return m.getPattern().matches(path);
        }).collect(Collectors.toList());
    }

    /**
     * Maps all controller methods when the class loads
     */
    private void mapsAllControllerMethods() {
        Method[] mList = c.getDeclaredMethods();
        for (Method m : mList) {
            if (m.getParameterCount() != 2) {
                continue;
            }
            Parameter[] param = m.getParameters();
            if (param[0].getType() != HttpServletRequest.class && param[1].getType() != HttpServletRequest.class) {
                continue;
            }
            if (param[0].getType() != HttpServletResponse.class && param[1].getType() != HttpServletResponse.class) {
                continue;
            }
            // Adds the method on list
            methods.add(new ControllerMethod(this, m));
        }
    }

}
