package de.alphahelix.alphalibary.server.netty.server;

import de.alphahelix.alphalibary.server.netty.RequestProcessor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.HashMap;
import java.util.Map;

public class EchoServer {

    private static final Map<String, RequestProcessor> REPROCESSOR_MAP = new HashMap<>();
    private int port;
    private ChannelFuture f;
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    public EchoServer(int port) {
        this.port = port;
    }

    public static void addRequestProcessor(String data, RequestProcessor requestProcessor) {
        REPROCESSOR_MAP.put(data, requestProcessor);
    }

    public static RequestProcessor process(String data) {
        return REPROCESSOR_MAP.get(data);
    }

    public void start() {
        ServerBootstrap b = new ServerBootstrap();

        b.group(workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel (SocketChannel socketChannel) {
                        System.out.println("New client connected! (" + socketChannel.localAddress() + ")");

                        socketChannel.pipeline().addLast(new StringEncoder()).addLast(new StringEncoder()).addLast(new EchoServerHandler());
                    }
                });

        f = b.bind(port);
    }

    public void stop() {
        try {
            f.channel().close().sync();
            workerGroup.shutdownGracefully();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
