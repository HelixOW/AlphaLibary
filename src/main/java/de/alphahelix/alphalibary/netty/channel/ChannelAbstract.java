package de.alphahelix.alphalibary.netty.channel;

import de.alphahelix.alphalibary.netty.IPacketListener;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class ChannelAbstract {

    static final Class<?> EntityPlayer = ReflectionUtil.getNmsClass("EntityPlayer");
    static final Class<?> PlayerConnection = ReflectionUtil.getNmsClass("PlayerConnection");
    //    static final Class<?> NetworkManager = ReflectionUtil.getNmsClass("NetworkManager");
    static final Class<?> Packet = ReflectionUtil.getNmsClass("Packet");
    static final Class<?> ServerConnection = ReflectionUtil.getNmsClass("ServerConnection");
    static final Class<?> MinecraftServer = ReflectionUtil.getNmsClass("MinecraftServer");

    static final ReflectionUtil.SaveField
            networkManager = ReflectionUtil.getDeclaredField("networkManager", PlayerConnection);

    static final ReflectionUtil.SaveField
            playerConnection = ReflectionUtil.getDeclaredField("playerConnection", EntityPlayer);

    static final ReflectionUtil.SaveField
            serverConnection = ReflectionUtil.getFirstType(ServerConnection, MinecraftServer);

    static final ReflectionUtil.SaveField
            connectionList = ReflectionUtil.getLastType(List.class, ServerConnection);

    static final ReflectionUtil.SaveMethod
            getServer = ReflectionUtil.getDeclaredMethod("getServer", Bukkit.getServer().getClass());

    static final String KEY_HANDLER = "packet_handler";
    static final String KEY_PLAYER = "packet_listener_player";
    static final String KEY_SERVER = "packet_listener_server";

    final Executor addChannelExecutor = Executors.newSingleThreadExecutor();
    final Executor removeChannelExecutor = Executors.newSingleThreadExecutor();
    private IPacketListener iPacketListener;

    public ChannelAbstract(IPacketListener iPacketListener) {
        this.iPacketListener = iPacketListener;
    }

    public abstract void addChannel(Player player);

    public abstract void removeChannel(Player player);

    public void addServerChannel() {
        try {
            Object dedicatedServer = getServer.invoke(Bukkit.getServer(), false);
            if (dedicatedServer == null) return;

            Object serverConnection = ChannelAbstract.serverConnection.get(dedicatedServer);
            if (serverConnection == null) return;

            List currentList = (List) connectionList.get(serverConnection);
            ReflectionUtil.SaveField superListField = new ReflectionUtil.SaveField(currentList.getClass().getSuperclass().getDeclaredField("list"));
            Object list = superListField.get(currentList);
            if (IListenerList.class.isAssignableFrom(list.getClass())) return;

            List newList = Collections.synchronizedList(newListenerList());
            for (Object o : currentList) {
                newList.add(o);
            }

            connectionList.set(serverConnection, newList, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract IListenerList newListenerList();

    protected final Object onPacketSend(Object receiver, Object packet, Cancellable cancellable) {
        return iPacketListener.onPacketSend(receiver, packet, cancellable);
    }

    protected final Object onPacketReceive(Object sender, Object packet, Cancellable cancellable) {
        return iPacketListener.onPacketReceive(sender, packet, cancellable);
    }

    interface IListenerList<E> extends List<E> {
    }

    interface IChannelHandler {
    }

    interface IChannelWrapper {
    }
}
