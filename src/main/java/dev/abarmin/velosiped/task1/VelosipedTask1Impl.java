package dev.abarmin.velosiped.task1;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class VelosipedTask1Impl implements VelosipedTask1 {

    private static final HttpHandler sumEndpoint=t->{
        var query = t.getRequestURI().getQuery();
        String[] params = query.split("&");
        int a = Integer.parseInt(params[0].split("=")[1]);
        int b = Integer.parseInt(params[1].split("=")[1]);

        String response = "" + (a + b);
        t.sendResponseHeaders(200, response.length());
        var os = t.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    };


    private HttpServer server;
    @Override
    public void startServer(int port) {
        if (server != null) {
            throw new RuntimeException("Please, stop server ");
        }
         try {
             server = HttpServer.create(new InetSocketAddress(port), 0);
         } catch (IOException e) {
             e.printStackTrace();
             throw new RuntimeException();
         }
        server.createContext("/sum", sumEndpoint);
        server.setExecutor(null);
        server.start();
    }

    @Override
    public void stopServer() {
        if (server == null) {
            throw new RuntimeException("Please, start the server");
        }
        server.stop(1);
        server = null;
    }

}
