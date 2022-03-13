package dev.abarmin.velosiped.task2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

@SuppressWarnings("deprecation")
public class VelosipedTask2Impl implements VelosipedTask2 {
    HttpServer server;

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final HttpHandler sumEndpoint = t -> {
        Request request = objectMapper.readValue(t.getRequestBody(), Request.class);
        Response response = new Response(request.getArg1() + request.getArg2());
        byte[] responseBytes = objectMapper.writeValueAsBytes(response);
        t.sendResponseHeaders(200, responseBytes.length);
        OutputStream os = t.getResponseBody();
        os.write(responseBytes);
        os.close();
    };

    @Override
    public void startServer(int port)  {
    if(server!=null){
    throw new RuntimeException("Please, stop server");
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
            throw new RuntimeException("Please, start thr server");
        }
        server.stop(1);
        server = null;
    }

}
