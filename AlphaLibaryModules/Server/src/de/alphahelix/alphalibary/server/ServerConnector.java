package de.alphahelix.alphalibary.server;

import de.alphahelix.alphalibary.server.netty.NettyCallback;
import de.alphahelix.alphalibary.server.netty.RequestProcessor;
import de.alphahelix.alphalibary.server.netty.client.EchoClient;
import de.alphahelix.alphalibary.server.netty.server.EchoServer;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ServerConnector {

    private static EchoServer server;
    private static EchoClient client;

    public ServerConnector(int ownPort, String host, int port) {
        if (ownPort == port) {
            System.out.println("Cannot connect to own server!");
            return;
        }

        server = new EchoServer(ownPort);
        client = new EchoClient(host, port);

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

        executor.execute(() -> {
            try {
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public EchoServer getOwnServer() {
        return server;
    }

    public EchoClient getClient() {
        return client;
    }

    public void makeRequest(String request, NettyCallback callback) {
        getClient().request(request, callback);
    }

    public void addRequestProcessor(String request, RequestProcessor reprocessor) {
        EchoServer.addRequestProcessor(request, reprocessor);
    }
}
