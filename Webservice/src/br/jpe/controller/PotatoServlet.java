/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A Simple Hello Potato Servlet to help with tests
 *
 * @author joaovperin
 */
@WebServlet("/")
public class PotatoServlet extends HttpServlet {

    /**
     * Answers to GET Requests
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        try (PrintWriter w = res.getWriter()) {
            w.append("<html>");
            w.append("<head>");
            w.append("<title>").append("Potato").append("</title>");
            w.append("</head>");
            w.append("<body>");
            w.append("<h1>").append("My Hello Potato Page :D").append("</h1>");
            w.append("</body>");
            w.append("</html>");
            w.flush();
        }
    }

}
