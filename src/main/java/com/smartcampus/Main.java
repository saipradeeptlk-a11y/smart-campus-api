package com.smartcampus;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import java.net.URI;
import java.io.IOException;

public class Main {

    // Base URL where your API will run
    public static final String BASE_URI = "http://localhost:8080/api/v1/";

    public static void main(String[] args) throws IOException {

        // Tell Jersey where to find your resource classes
        final ResourceConfig config = new ResourceConfig()
                .packages(
                    "com.smartcampus.resources",    // scans for @Path classes
                    "com.smartcampus.exceptions"     // scans for @Provider classes
                )
                .register(JacksonFeature.class);     // enables JSON support

        // Start the Grizzly HTTP server
        final HttpServer server = GrizzlyHttpServerFactory
                .createHttpServer(URI.create(BASE_URI), config);

        System.out.println("===========================================");
        System.out.println("Smart Campus API is running!");
        System.out.println("URL: " + BASE_URI );
        System.out.println("Press ENTER to stop the server...");
        System.out.println("===========================================");

        // Keep server running until ENTER is pressed
        System.in.read();
        server.stop();
        System.out.println("Server stopped.");
    }
}
