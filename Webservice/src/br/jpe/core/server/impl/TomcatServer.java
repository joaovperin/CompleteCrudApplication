/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.server.impl;

import br.jpe.core.server.AbstractServer;
import br.jpe.core.utils.FileX;
import java.io.File;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

/**
 * TomcatServer implementation
 *
 * @author joaovperin
 */
public class TomcatServer extends AbstractServer {

    /** Webapps directory */
    private static final String WEB_APPS = "WEB-APP";
    /** Classes directory */
    private static final String CLASSES = "/WEB-INF/classes";
    /** Additional info */
    private static final File ADD_INFO = new File("build/classes");

    /** Tomcat instance */
    private Tomcat tomcat;

    /**
     * Runs the server
     */
    @Override
    public void run() {
        System.out.println("*** Server starting...");
        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * Stops the server
     */
    @Override
    public void stop() {
        System.out.println("*** Server stopping...");
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * Configures the server
     */
    @Override
    public void configure() {
        System.out.println("*** Server configuring...");
        options.printAll(System.out);
        // Prepares the folders structure
        prepareFolderStructure();
        try {
            // Instantiate Tomcat and configure the port
            this.tomcat = new Tomcat();
            tomcat.setPort(options.getPort());
            // Configures the context
            StandardContext ctx = (StandardContext) tomcat.addWebapp("", WEB_APPS);
            WebResourceRoot resources = new StandardRoot(ctx);
            resources.addPreResources(new DirResourceSet(resources, CLASSES, ADD_INFO.getAbsolutePath(), "/"));
            ctx.setResources(resources);
        } catch (ServletException e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * Prepares the folder structure
     */
    private static void prepareFolderStructure() {
        FileX.createDirIfNotExists(new File(WEB_APPS));
        FileX.createDirIfNotExists(new File(CLASSES));
        FileX.createDirIfNotExists(ADD_INFO);
    }

}
