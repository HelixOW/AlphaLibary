package io.github.alphahelixdev.alpary.reflection.nms.nettyinjection.handler;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class PacketHandler {

	private static final List<PacketHandler> HANDLERS = new ArrayList<>();
	private boolean hasSendOptions, forceSendPlayer, forceSendServer, hasReceiveOptions, forcePlayerReceive,
			forceServerReceive;
	private JavaPlugin plugin;

	public PacketHandler(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public static boolean addHandler(PacketHandler handler) {
		boolean b = HANDLERS.contains(handler);

		if(!b) {
			PacketOptions opt =
					NMSUtil.getReflections().getMethod("onSend", handler.getClass(), SentPacket.class).asNormal()
					       .getAnnotation(PacketOptions.class);

			if(opt != null) {
				handler.hasSendOptions = true;

				if(opt.forcePlayer() && opt.forceServer())
					throw new IllegalArgumentException("Can't force player and server packet at the same time");

				if(opt.forcePlayer())
					handler.forceSendPlayer = true;
				else if(opt.forceServer())
					handler.forceSendServer = true;
			}

			opt = NMSUtil.getReflections().getMethod("onReceive", handler.getClass(), ReceivedPacket.class)
			             .asNormal().getAnnotation(PacketOptions.class);

			if(opt != null) {
				handler.hasReceiveOptions = true;

				if(opt.forcePlayer() && opt.forceServer())
					throw new IllegalArgumentException("Can't force player and server packet at the same time");

				if(opt.forcePlayer())
					handler.forcePlayerReceive = true;
				else if(opt.forceServer())
					handler.forceServerReceive = true;
			}
		}

		HANDLERS.add(handler);
		return !b;
	}

	public static boolean removeHandler(PacketHandler handler) {
		return HANDLERS.remove(handler);
	}

	public static void notifyHandlers(SentPacket packet) {
		HANDLERS.stream().filter(PacketHandler::hasSendOptions)
		        .filter(packetHandler -> (packetHandler.isForceSendPlayer() && packet.hasPlayer())
				        || (packetHandler.isForceSendServer() && packet.hasChannel()))
		        .forEach(packetHandler -> packetHandler.onSend(packet));
	}

	public boolean hasSendOptions() {
		return hasSendOptions;
	}

	public boolean isForceSendPlayer() {
		return forceSendPlayer;
	}

	public boolean isForceSendServer() {
		return forceSendServer;
	}

	public abstract void onSend(SentPacket var1);

	public static void notifyHandlers(ReceivedPacket packet) {
		HANDLERS.stream().filter(PacketHandler::hasReceiveOptions)
		        .filter(packetHandler -> (packetHandler.isForcePlayerReceive() && packet.hasPlayer())
				        || (packetHandler.isForceServerReceive() && packet.hasChannel()))
		        .forEach(packetHandler -> packetHandler.onReceive(packet));
	}

	public boolean hasReceiveOptions() {
		return hasReceiveOptions;
	}

	public boolean isForcePlayerReceive() {
		return forcePlayerReceive;
	}

	public boolean isForceServerReceive() {
		return forceServerReceive;
	}

	public abstract void onReceive(ReceivedPacket var1);

	public static List<PacketHandler> getHandlersForPlugin(JavaPlugin plugin) {
		if(plugin == null)
			return HANDLERS;

		return HANDLERS.stream().filter(packetHandler -> packetHandler.getPlugin().equals(plugin))
		               .collect(Collectors.toList());
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public static List<PacketHandler> handlers() {
		return HANDLERS;
	}

	@Override
	public int hashCode() {
		return Objects.hash(hasSendOptions, forceSendPlayer, forceSendServer, hasReceiveOptions, forcePlayerReceive,
		                    forceServerReceive);
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		PacketHandler that = (PacketHandler) o;
		return hasSendOptions == that.hasSendOptions &&
				forceSendPlayer == that.forceSendPlayer &&
				forceSendServer == that.forceSendServer &&
				hasReceiveOptions == that.hasReceiveOptions &&
				forcePlayerReceive == that.forcePlayerReceive &&
				forceServerReceive == that.forceServerReceive;
	}

	@Override
	public String toString() {
		return "PacketHandler{" +
				"hasSendOptions=" + hasSendOptions +
				", forceSendPlayer=" + forceSendPlayer +
				", forceSendServer=" + forceSendServer +
				", hasReceiveOptions=" + hasReceiveOptions +
				", forcePlayerReceive=" + forcePlayerReceive +
				", forceServerReceive=" + forceServerReceive +
				", plugin=" + plugin +
				'}';
	}
}
