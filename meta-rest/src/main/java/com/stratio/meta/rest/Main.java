package com.stratio.meta.rest;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.stratio.meta.common.exceptions.ConnectionException;
import com.stratio.meta.rest.utils.DriverHelper;

/**
 * Main class.
 * 
 */
public class Main {
  // Base URI the Grizzly HTTP server will listen on
  public static final String BASE_URI = "http://localhost:8180/";

  // Driver that connects to the META servers.

  private static DriverHelper driver = DriverHelper.getInstance();

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   * 
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer() {
    // create a resource config that scans for JAX-RS resources and providers
    // in com.stratio.meta.rest package
    final ResourceConfig rc = new ResourceConfig().packages("com.stratio.meta.rest");

    // create and start a new instance of grizzly http server
    // exposing the Jersey application at BASE_URI
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
  }

  /**
   * Main method.
   * 
   * @param args
   * @throws IOException
   * @throws ConnectionException
   */
  public static void main(String[] args) throws IOException {
    final HttpServer server = startServer();
    try {
      driver.connect();
      System.out.println(String.format("Jersey app started with WADL available at "
          + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
      System.in.read();
      server.stop();
      driver.close();
    } catch (ConnectionException e) {
      System.out.println("Couldn't connect");
      server.stop();
      driver.close();
    }

  }
}