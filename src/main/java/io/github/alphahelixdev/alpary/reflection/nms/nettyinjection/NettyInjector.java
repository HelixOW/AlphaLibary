package io.github.alphahelixdev.alpary.reflection.nms.nettyinjection;

import io.github.alphahelixdev.alpary.reflection.nms.nettyinjection.channel.ChannelWrapper;
import io.github.alphahelixdev.alpary.reflection.nms.nettyinjection.handler.PacketHandler;
import io.github.alphahelixdev.alpary.reflection.nms.nettyinjection.handler.ReceivedPacket;
import io.github.alphahelixdev.alpary.reflection.nms.nettyinjection.handler.SentPacket;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class NettyInjector implements INettyInjector, Listener {
	
	private final Logger logger = Logger.getLogger("NettyInjector");
	protected boolean injected = false;
	private ChannelInjector channelInjector;
	
	public static boolean addPacketHandler(PacketHandler packetHandler) {
		return PacketHandler.addHandler(packetHandler);
	}
	
	public static boolean removePacketHandler(PacketHandler packetHandler) {
		return PacketHandler.removeHandler(packetHandler);
	}
	
	public void load() {
		this.channelInjector = new ChannelInjector();
		if(this.injected = this.channelInjector.inject(this)) {
			this.channelInjector.addServerChannel();
			this.logger.info("Injected custom channel handlers");
		} else {
			throw new RuntimeException("Failed to inject custom handlers");
		}
	}
	
	public void enable(JavaPlugin plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		
		this.logger.info("Starting to inject channels for online players...");
		Bukkit.getOnlinePlayers().forEach(this.channelInjector::addChannel);
		this.logger.info("Done injecting channels for online players!");
	}
	
	public void disable() {
		if(!this.injected)
			return;
		
		this.logger.info("Starting to remove channels for online players...");
		Bukkit.getOnlinePlayers().forEach(this.channelInjector::removeChannel);
		this.logger.info("Done removing channels for online players...");
		
		this.logger.info("Removing packet handlers (" + PacketHandler.handlers().size() + ")...");
		while(!PacketHandler.handlers().isEmpty())
			PacketHandler.removeHandler(PacketHandler.handlers().get(0));
		
		this.logger.info("Removed all packet handlers!");
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		this.channelInjector.addChannel(e.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		this.channelInjector.removeChannel(e.getPlayer());
	}
	
	@Override
	public Object onPacketSend(Object receiver, Object packet, Cancellable cancellable) {
		SentPacket sentPacket;
		
		if(receiver instanceof Player)
			sentPacket = new SentPacket(packet, cancellable, (Player) receiver);
		else
			sentPacket = new SentPacket(packet, cancellable, (ChannelWrapper) receiver);
		
		PacketHandler.notifyHandlers(sentPacket);
		return sentPacket.getPacket() != null ? sentPacket.getPacket() : packet;
	}
	
	@Override
	public Object onPacketReceive(Object sender, Object packet, Cancellable cancellable) {
		ReceivedPacket receivedPacket;
		if(sender instanceof Player)
			receivedPacket = new ReceivedPacket(packet, cancellable, (Player) sender);
		else
			receivedPacket = new ReceivedPacket(packet, cancellable, (ChannelWrapper) sender);
		
		PacketHandler.notifyHandlers(receivedPacket);
		return receivedPacket.getPacket() != null ? receivedPacket.getPacket() : packet;
	}
}
