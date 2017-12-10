package de.alphahelix.alphalibary.server.netty.client;

import com.google.gson.JsonParser;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext ctx;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.ctx = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object s) {
        String[] sentDataAndJson = ((String) s).split("-::=::-");
        String data = sentDataAndJson[0];
        String json = sentDataAndJson[1];

        if (EchoClient.getRequests().containsKey(data))
            EchoClient.getRequests().get(data).accept(new JsonParser().parse(json));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public void requestData(String data) {
        if (ctx != null) {
            try {
                ChannelFuture f = ctx.writeAndFlush(Unpooled.copiedBuffer(data, CharsetUtil.UTF_8)).sync();

                if (!f.isSuccess())
                    try {
                        throw f.cause();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
