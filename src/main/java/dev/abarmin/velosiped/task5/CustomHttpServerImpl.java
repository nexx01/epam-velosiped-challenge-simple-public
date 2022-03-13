package dev.abarmin.velosiped.task5;

import com.sun.net.httpserver.HttpServer;
import dev.abarmin.velosiped.task2.Request;
import dev.abarmin.velosiped.task4.CustomHttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class CustomHttpServerImpl implements CustomHttpServer {
    private HttpServer httpServer;
    @Override
    public Request parseRequestParameters(String httpRequest) {
        return null;
    }

    @Override
    public String createHttpResponse(String responseBody) {
        return null;
    }

    @Override
    public void startServer(int port) {
        try {
            httpServer = HttpServer.create(new InetSocketAddress("localhost",
                    port), 0);
            httpServer.setExecutor(null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopServer() {
        httpServer.stop(1);
    }
}
