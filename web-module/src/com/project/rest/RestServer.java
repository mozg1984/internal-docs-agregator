package com.project.rest;

import com.project.configuration.Configurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class RestServer {
  private int port;
  private Server jettyServer;
  
  public RestServer() {
    port = Configurator.getInt("rest-server.port");
    jettyServer = new Server(port);
  }

  public void start() throws Exception {
    init();
    
    try {
        jettyServer.start();
        jettyServer.join();
    } finally {
        jettyServer.destroy();
    }
  }

  public void stop() throws Exception {
    try {
        jettyServer.stop();
    } finally {
        jettyServer.destroy();
    }
  }
  
  private void init() {
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    jettyServer.setHandler(context);

    ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
    jerseyServlet.setInitOrder(0);
    
    // Tells the Jersey Servlet which REST service/class to load.
    jerseyServlet.setInitParameter(
      "jersey.config.server.provider.classnames",
      Document.class.getCanonicalName()
    );
  }
}