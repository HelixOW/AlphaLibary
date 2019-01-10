package io.github.alphahelixdev.alpary.reflection.nms.nettyinjection.channel;

import io.github.alphahelixdev.alpary.reflection.nms.nettyinjection.INettyInjector;
import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.Reflections;
import io.github.alphahelixdev.helius.reflection.SaveField;
import io.github.alphahelixdev.helius.reflection.SaveMethod;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class ChannelAbstract {

	protected static final Class<?> NETWORK_MANAGER_CLASS = Utils.nms().getNMSClass("NetworkManager");
	protected static final Class<?> PACKET_CLASS = Utils.nms().getNMSClass("Packet");
	private static final Reflections REFLECTIONS = NMSUtil.getReflections();
	private static final Class<?> ENTITY_PLAYER_CLASS = Utils.nms().getNMSClass("EntityPlayer");
	protected static final SaveField PLAYER_CONNECTION = REFLECTIONS.getDeclaredField("playerConnection",
	                                                                                  ENTITY_PLAYER_CLASS);
	private static final Class<?> PLAYER_CONNECTION_CLASS = Utils.nms().getNMSClass("PlayerConnection");
	protected static final SaveField NETWORK_MANAGER = REFLECTIONS.getDeclaredField("networkManager",
	                                                                                PLAYER_CONNECTION_CLASS);
	private static final Class<?> SERVER_CONNECTION_CLASS = Utils.nms().getNMSClass("ServerConnection");
	private static final Class<?> MINECRAFT_SERVER_CLASS = Utils.nms().getNMSClass("MinecraftServer");
	private static final SaveField SERVER_CONNECTION =
			REFLECTIONS.getFirstDeclaredFieldWithType(SERVER_CONNECTION_CLASS, MINECRAFT_SERVER_CLASS);
	private static final SaveField CONNECTION_LIST = REFLECTIONS.getLastDeclaredFieldWithType(List.class,
	                                                                                          SERVER_CONNECTION_CLASS);

	private static final SaveMethod GET_SERVER = REFLECTIONS.getDeclaredMethod("getServer",
	                                                                           Bukkit.getServer().getClass());

	private static final String KEY_HANDLER = "packet_handler";
	private static final String KEY_PLAYER = "packet_listener_player";
	private static final String KEY_SERVER = "packet_listener_server";

	protected final Executor addChannelExecutor = Executors.newSingleThreadExecutor();
	protected final Executor removeChannelExecutor = Executors.newSingleThreadExecutor();

	private INettyInjector iNettyInjector;

	public ChannelAbstract(INettyInjector iNettyInjector) {
		this.iNettyInjector = iNettyInjector;
	}

	public abstract void addChannel(Player p);

	public abstract void removeChannel(Player p);

	public void addServerChannel() {
		try {
			Object e = GET_SERVER.invoke(Bukkit.getServer(), false);

			if(e == null)
				return;

			Object serverConnection = SERVER_CONNECTION.get(e);
			if(serverConnection == null)
				return;

			List currentList = (List) CONNECTION_LIST.get(serverConnection);
			SaveField superListField = REFLECTIONS.getDeclaredField("list",
			                                                        currentList.getClass().getSuperclass());
			Object list = superListField.get(currentList);
			if(ChannelAbstract.IListenerList.class.isAssignableFrom(list.getClass()))
				return;

			List newList = Collections.synchronizedList(this.newListenerList());
			newList.addAll(currentList);

			CONNECTION_LIST.set(serverConnection, newList);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public abstract ChannelAbstract.IListenerList newListenerList();

	protected Object onPacketSend(Object receiver, Object packet, Cancellable cancellable) {
		return this.iNettyInjector.onPacketSend(receiver, packet, cancellable);
	}

	protected Object onPacketReceive(Object sender, Object packet, Cancellable cancellable) {
		return this.iNettyInjector.onPacketReceive(sender, packet, cancellable);
	}

	interface IListenerList extends List {

	}

	interface IChannelHandler {

	}

	interface IChannelWrapper {

	}
}
