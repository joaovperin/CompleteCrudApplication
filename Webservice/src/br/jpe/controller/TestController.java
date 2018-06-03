/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.controller;

import br.jpe.core.servlet.ControllerClass;
import br.jpe.core.servlet.Pattern;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Test Controller
 *
 * @author joaovperin
 */
public class TestController extends ControllerClass<TestController> {

    /**
     * This controller URL pattern
     *
     * @return Pattern
     */
    @Override
    public Pattern getPattern() {
        return Pattern.create("/test/*");
    }

    /**
     * Hehe method
     * 
     * @param req
     * @param res
     */
    public void hehe(HttpServletRequest req, HttpServletResponse res) {
        System.out.println("******* HEHE");
        try {
            try (PrintWriter writer = res.getWriter()) {
                writer.append("UEUEUE EXECUTEI O HEHE METHOD.");
                writer.flush();
            }
        } catch (IOException ex) {
            throw new RuntimeException("falhdald", ex);
        }

    }

}
