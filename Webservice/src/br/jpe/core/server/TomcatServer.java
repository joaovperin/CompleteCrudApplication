/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.server;

import java.io.File;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

/**
 *
 * @author programacao
 */
public class TomcatServer extends AbstractServer {

    private static final String WEB_APPS = "WEB-APP";
    private static final String CLASSES = "/WEB-INF/classes";
    private static final File ADD_INFO = new File("build/classes");

    /** Tomcat instance */
    private Tomcat tomcat;

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

    @Override
    public void stop() {
        System.out.println("*** Server stopping...");
    }

    @Override
    public void configure() {
        System.out.println("*** Server configuring...");
        options.printAll(System.out);
        // Prepares the folders structure
        prepareFolderStructure();
        try {
            // Instantiate Tomcat and configure the port
            this.tomcat = new Tomcat();
            tomcat.setPort(Integer.valueOf(options.get("port", "8085")));
            // Configures the context
            StandardContext ctx = (StandardContext) tomcat.addWebapp("", WEB_APPS);
            WebResourceRoot resources = new StandardRoot(ctx);
            resources.addPreResources(new DirResourceSet(resources, CLASSES, ADD_INFO.getAbsolutePath(), "/"));
            ctx.setResources(resources);
        } catch (ServletException e) {
            e.printStackTrace(System.out);
        }
    }

    private static void prepareFolderStructure() {
        createDirIfNotExists(new File(WEB_APPS));
        createDirIfNotExists(new File(CLASSES));
        createDirIfNotExists(ADD_INFO);
    }

    private static void createDirIfNotExists(File f) {
        if (!f.exists() && f.isDirectory()) {
            System.out.println("*** Creating directory ".concat(f.getName()).concat(" ..."));
            f.mkdir();
        }
    }

}
