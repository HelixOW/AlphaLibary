package de.alphahelix.alphalibary.server.netty.client;

import de.alphahelix.alphalibary.server.netty.NettyCallback;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.ConcurrentHashMap;

public class EchoClient {

    //Request name -> CallBack
    private static final ConcurrentHashMap<String, NettyCallback> REQUESTS = new ConcurrentHashMap<>();
    private EchoClientHandler ech = new EchoClientHandler();

    public EchoClient(String host, int port) {
        EventLoopGroup worker = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();

        b.group(worker)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(ech);
                    }
                });

        b.connect(host, port);
    }

    public static ConcurrentHashMap<String, NettyCallback> getRequests() {
        return REQUESTS;
    }

    public void request(String sentData, NettyCallback nettyCallback) {
        ech.requestData(sentData);
        REQUESTS.put(sentData, nettyCallback);
    }
}
