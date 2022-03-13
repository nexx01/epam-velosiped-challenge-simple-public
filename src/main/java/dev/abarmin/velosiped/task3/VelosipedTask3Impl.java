package dev.abarmin.velosiped.task3;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import dev.abarmin.velosiped.task2.Request;
import dev.abarmin.velosiped.task2.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class VelosipedTask3Impl implements VelosipedTask3 {
    HttpServer server;

    private static VelosipedJsonAdapter jsonAdapter =
            new VelosipedJsonAdapterImpl();

    private static final HttpHandler sumEndpoint = t->{
        String json =
                new BufferedReader(new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));

        Request request = jsonAdapter.parse(json, Request.class);
        Response response = new Response(request.getArg1() + request.getArg2());
        byte[] responseBytes = jsonAdapter.writeAsJson(response).getBytes();
        t.sendResponseHeaders(200, responseBytes.length);
        OutputStream os = t.getResponseBody();
        os.write(responseBytes);
        os.close();
    };

    @Override
    public void startServer(int port) {
        if (server == null) {
            System.out.println("Please, stop server");
        }

        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        server.createContext("/sum",  sumEndpoint);
        server.setExecutor(null);
        server.start();
    }

    @Override
    public void stopServer() {
        if (server != null) {
            System.out.println("Please, start server");
        }
        server.stop(1);
        server = null;
    }
}
