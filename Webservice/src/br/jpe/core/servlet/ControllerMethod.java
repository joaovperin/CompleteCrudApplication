/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.servlet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A Controller Method wrapper
 *
 * @author joaovperin
 */
public final class ControllerMethod {

    /** Controller class */
    private final ControllerClass controllerClass;
    /** Java Class Method */
    private final Method clazzMethod;

    /**
     * Constructor
     *
     * @param controllerClass
     * @param clazzMethod
     */
    public ControllerMethod(ControllerClass controllerClass, Method clazzMethod) {
        this.controllerClass = controllerClass;
        this.clazzMethod = clazzMethod;
    }

    /**
     * Returns the URL Pattern
     *
     * @return Pattern
     */
    public Pattern getPattern() {
        String preffix = new StringBuilder("/").
                append(controllerClass.getClass().getSimpleName().toLowerCase()).
                append("/").
                toString();
        return controllerClass.getPattern().subPattern(preffix.concat(clazzMethod.getName()));
    }

    /**
     * Executes the method
     *
     * @param request
     * @param response
     */
    public void exec(HttpServletRequest request, HttpServletResponse response) {
        try {
            clazzMethod.invoke(controllerClass, request, response);
        } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            throw new RuntimeException("Fail to execute controller method", e);
        }
    }

}
