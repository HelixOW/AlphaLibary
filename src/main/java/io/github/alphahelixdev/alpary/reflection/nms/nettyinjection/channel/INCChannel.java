package io.github.alphahelixdev.alpary.reflection.nms.nettyinjection.channel;

import io.github.alphahelixdev.alpary.reflection.nms.nettyinjection.CancellablePacket;
import io.github.alphahelixdev.alpary.reflection.nms.nettyinjection.INettyInjector;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.helix.reflection.SaveField;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Player;

import java.net.SocketAddress;
import java.util.ArrayList;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class INCChannel extends ChannelAbstract {

    private static final SaveField channelField = Utils.nms()
			.getFirstDeclaredFieldWithType(Channel.class,
					NETWORK_MANAGER_CLASS);
	
	public INCChannel(INettyInjector iNettyInjector) {
		super(iNettyInjector);
	}
	
	@Override
	public void addChannel(Player p) {
		Channel e = this.getChannel(p);
		this.addChannelExecutor.execute(() -> e.pipeline().addBefore("packet_handler", "packet_listener_player",
				INCChannel.this.new ChannelHandler(p)));
	}
	
	@Override
	public void removeChannel(Player p) {
		Channel e = this.getChannel(p);
		this.removeChannelExecutor.execute(() -> {
			if(e.pipeline().get("packet_listener_player") != null)
				e.pipeline().remove("packet_listener_player");
		});
	}
	
	@Override
	public IListenerList newListenerList() {
		return new INCChannel.ListenerList();
	}
	
	private Channel getChannel(Player player) {
		Object playerHandle = Utils.nms().getCraftPlayer(player);
		Object connection = PLAYER_CONNECTION.get(playerHandle);
		return (Channel) channelField.get(NETWORK_MANAGER.get(connection));
	}
	
	class ListenerList extends ArrayList implements ChannelAbstract.IListenerList {
		
		@Override
		public boolean add(Object o) {
			INCChannel.this.addChannelExecutor.execute(() -> {
				Channel channel = null;
				
				while(channel == null)
					channel = (Channel) INCChannel.channelField.get(o);
				
				if(channel.pipeline().get("packet_listener_server") == null)
					channel.pipeline().addBefore("packet_handler", "packet_listener_server",
							INCChannel.this.new ChannelHandler(
									INCChannel.this.new INCChannelWrapper(channel)));
			});
			
			return super.add(o);
		}
		
		@Override
		public boolean remove(Object o) {
			INCChannel.this.removeChannelExecutor.execute(() -> {
				Channel channel = null;
				
				while(channel == null)
					channel = (Channel) INCChannel.channelField.get(o);
				
				if(channel.pipeline().get("packet_listener_server") != null)
					channel.pipeline().remove("packet_listener_server");
			});
			
			return super.remove(o);
		}
	}
	
	class ChannelHandler extends ChannelDuplexHandler implements ChannelAbstract.IChannelHandler {
		
		private Object owner;
		
		public ChannelHandler(Player player) {
			this.owner = player;
		}
		
		public ChannelHandler(ChannelWrapper channelWrapper) {
			this.owner = channelWrapper;
		}
		
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
			CancellablePacket cancellable = new CancellablePacket();
			Object pckt = msg;
			if(ChannelAbstract.PACKET_CLASS.isAssignableFrom(msg.getClass()))
				pckt = INCChannel.this.onPacketSend(this.owner, msg, cancellable);
			
			if(!cancellable.isCancelled())
				super.write(ctx, pckt, promise);
		}
		
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			CancellablePacket cancellable = new CancellablePacket();
			Object pckt = msg;
			if(ChannelAbstract.PACKET_CLASS.isAssignableFrom(msg.getClass()))
				pckt = INCChannel.this.onPacketReceive(this.owner, msg, cancellable);
			
			if(!cancellable.isCancelled())
				super.channelRead(ctx, pckt);
		}
		
		public String toString() {
			return "INCChannel$ChannelHandler@" + this.hashCode() + " (" + this.owner + ")";
		}
	}
	
	class INCChannelWrapper extends ChannelWrapper implements ChannelAbstract.IChannelWrapper {
		
		public INCChannelWrapper(Channel channel) {
			super(channel);
		}
		
		public SocketAddress getRemoteAddress() {
			return ((Channel) this.channel()).remoteAddress();
		}
		
		public SocketAddress getLocalAddress() {
			return ((Channel) this.channel()).localAddress();
		}
	}
}
