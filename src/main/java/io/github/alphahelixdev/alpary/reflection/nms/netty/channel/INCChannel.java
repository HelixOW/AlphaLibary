package io.github.alphahelixdev.alpary.reflection.nms.netty.channel;

import io.github.alphahelixdev.alpary.reflection.nms.netty.IPacketListener;
import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveField;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.net.SocketAddress;
import java.util.ArrayList;

public class INCChannel extends ChannelAbstract {

    private static final SaveField channelField = NMSUtil.getReflections().getFirstFieldWithType(Channel.class,
            Utils.nms().getNMSClass("NetworkManager"));

    public INCChannel(IPacketListener iPacketListener) {
        super(iPacketListener);
    }

    public static SaveField getChannelField() {
        return INCChannel.channelField;
    }

    public void addChannel(final Player player) {
        final Channel channel = this.getChannel(player);
        this.getAddChannelExecutor().execute(() -> {
            try {
                channel.pipeline().addBefore(ChannelAbstract.getKeyHandler(), ChannelAbstract.getKeyPlayer(),
                        INCChannel.this.new ChannelHandler(player));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void removeChannel(Player player) {
        final Channel channel = this.getChannel(player);
        this.getRemoveChannelExecutor().execute(() -> {
            try {
                if (channel.pipeline().get(ChannelAbstract.getKeyPlayer()) != null)
                    channel.pipeline().remove(ChannelAbstract.getKeyPlayer());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public ChannelAbstract.IListenerList newListenerList() {
        return new ListenerList();
    }

    private Channel getChannel(Player player) {
        Object handle = NMSUtil.getReflections().getDeclaredMethod("getHandle",
                Utils.nms().getCraftBukkitClass("entity.CraftPlayer")).invoke(player, false);
        Object connection = ChannelAbstract.getPlayerConnection().get(handle);
        return (Channel) INCChannel.getChannelField().get(ChannelAbstract.getNetworkManager().get(connection));
    }

    class ListenerList<E> extends ArrayList<E> implements ChannelAbstract.IListenerList<E> {

        public boolean add(E paramE) {
            try {
                final E a = paramE;
                INCChannel.this.getAddChannelExecutor().execute(() -> {
                    try {
                        Channel channel = null;
                        while (channel == null)
                            channel = (Channel) INCChannel.getChannelField().get(a);

                        if (channel.pipeline().get(ChannelAbstract.getKeyServer()) == null)
                            channel.pipeline().addBefore(ChannelAbstract.getKeyHandler(),
                                    ChannelAbstract.getKeyServer(), INCChannel.this.new ChannelHandler(
                                            INCChannel.this.new INCChannelWrapper(channel)));

                    } catch (Exception ignored) {
                    }
                });
            } catch (Exception ignored) {
            }
            return super.add(paramE);
        }

        public boolean remove(Object arg0) {
            try {
                final Object a = arg0;
                INCChannel.this.getRemoveChannelExecutor().execute(() -> {
                    try {
                        Channel channel = null;
                        while (channel == null)
                            channel = (Channel) INCChannel.getChannelField().get(a);

                        channel.pipeline().remove(ChannelAbstract.getKeyServer());
                    } catch (Exception ignored) {
                    }
                });
            } catch (Exception ignored) {
            }
            return super.remove(arg0);
        }
    }

    class ChannelHandler extends ChannelDuplexHandler implements ChannelAbstract.IChannelHandler {

        private final Object owner;

        public ChannelHandler(Player player) {
            this.owner = player;
        }

        public ChannelHandler(ChannelWrapper channelWrapper) {
            this.owner = channelWrapper;
        }

        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            Cancellable cancellable = new io.github.alphahelixdev.alpary.reflection.nms.netty.Cancellable();
            Object pckt = msg;
            if (ChannelAbstract.getPacketClass().isAssignableFrom(msg.getClass()))
                pckt = INCChannel.this.onPacketSend(this.getOwner(), msg, cancellable);


            if (cancellable.isCancelled())
                return;

            super.write(ctx, pckt, promise);
        }

        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Cancellable cancellable = new io.github.alphahelixdev.alpary.reflection.nms.netty.Cancellable();
            Object pckt = msg;
            if (ChannelAbstract.getPacketClass().isAssignableFrom(msg.getClass()))
                pckt = INCChannel.this.onPacketReceive(this.getOwner(), msg, cancellable);

            if (cancellable.isCancelled())
                return;

            super.channelRead(ctx, pckt);
        }

        public Object getOwner() {
            return this.owner;
        }
    }

    class INCChannelWrapper extends ChannelWrapper<Channel> implements ChannelAbstract.IChannelWrapper {
        public INCChannelWrapper(Channel channel) {
            super(channel);
        }

        public SocketAddress getRemoteAddress() {
            return channel().remoteAddress();
        }

        public SocketAddress getLocalAddress() {
            return channel().localAddress();
        }
    }
}
