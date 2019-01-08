package io.github.alphahelixdev.alpary.reflection.nms.netty.channel;

import io.github.alphahelixdev.alpary.reflection.nms.netty.IPacketListener;
import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveField;
import io.github.alphahelixdev.helius.reflection.SaveMethod;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public abstract class ChannelAbstract {

    private static final String KEY_HANDLER = "packet_handler";
    private static final String KEY_PLAYER = "packet_listener_player";
    private static final String KEY_SERVER = "packet_listener_server";

    private static final Class<?> PACKET_CLASS = Utils.nms().getNMSClass("Packet");
    private static final Class<?> ENTITY_PLAYER_CLASS = Utils.nms().getNMSClass("EntityPlayer");
    private static final Class<?> PLAYER_CONNECTION_CLASS = Utils.nms().getNMSClass("PlayerConnection");
    private static final Class<?> SERVER_CONNECTION_CLASS = Utils.nms().getNMSClass("ServerConnection");
    private static final Class<?> MINECRAFT_SERVER_CLASS = Utils.nms().getNMSClass("MinecraftServer");

    private static final SaveField PLAYER_CONNECTION = NMSUtil.getReflections().getDeclaredField(
            "playerConnection", ENTITY_PLAYER_CLASS).removeFinal();
    private static final SaveField NETWORK_MANAGER = NMSUtil.getReflections().getDeclaredField(
            "networkManager", PLAYER_CONNECTION_CLASS).removeFinal();
    private static final SaveField SERVER_CONNECTION = NMSUtil.getReflections().getFirstDeclaredFieldWithType(
            MINECRAFT_SERVER_CLASS, SERVER_CONNECTION_CLASS).removeFinal();
    private static final SaveField CONNECTION_LIST = NMSUtil.getReflections().getLastFieldWithType(List.class,
            SERVER_CONNECTION_CLASS);
    private static final SaveMethod GET_SERVER = NMSUtil.getReflections().getDeclaredMethod("getServer",
            Bukkit.getServer().getClass());

    private final Executor addChannelExecutor = Executors.newSingleThreadExecutor();
    private final Executor removeChannelExecutor = Executors.newSingleThreadExecutor();
    private final IPacketListener iPacketListener;

    public ChannelAbstract(IPacketListener iPacketListener) {
        this.iPacketListener = iPacketListener;
    }

    static String getKeyHandler() {
        return ChannelAbstract.KEY_HANDLER;
    }

    static String getKeyPlayer() {
        return ChannelAbstract.KEY_PLAYER;
    }

    static String getKeyServer() {
        return ChannelAbstract.KEY_SERVER;
    }

    static Class<?> getPacketClass() {
        return ChannelAbstract.PACKET_CLASS;
    }

    static Class<?> getEntityPlayerClass() {
        return ChannelAbstract.ENTITY_PLAYER_CLASS;
    }

    static Class<?> getPlayerConnectionClass() {
        return ChannelAbstract.PLAYER_CONNECTION_CLASS;
    }

    static Class<?> getServerConnectionClass() {
        return ChannelAbstract.SERVER_CONNECTION_CLASS;
    }

    static Class<?> getMinecraftServerClass() {
        return ChannelAbstract.MINECRAFT_SERVER_CLASS;
    }

    static SaveField getPlayerConnection() {
        return ChannelAbstract.PLAYER_CONNECTION;
    }

    static SaveField getNetworkManager() {
        return ChannelAbstract.NETWORK_MANAGER;
    }

    static SaveField getServerConnection() {
        return ChannelAbstract.SERVER_CONNECTION;
    }

    static SaveField getConnectionList() {
        return ChannelAbstract.CONNECTION_LIST;
    }

    static SaveMethod getGetServer() {
        return ChannelAbstract.GET_SERVER;
    }

    public abstract void addChannel(Player player);

    public abstract void removeChannel(Player player);

    public void addServerChannel() {
        try {
            Object dedicatedServer = ChannelAbstract.getGetServer().invoke(Bukkit.getServer(), false);
            if (dedicatedServer == null) return;

            Object serverConnection = ChannelAbstract.getServerConnection().get(dedicatedServer);
            if (serverConnection == null) return;

            List currentList = (List) ChannelAbstract.getConnectionList().get(serverConnection);
            SaveField superListField = new SaveField(currentList.getClass().getSuperclass().getDeclaredField("list"));
            Object list = superListField.get(currentList);
            if (IListenerList.class.isAssignableFrom(list.getClass())) return;

            List newList = Collections.synchronizedList(newListenerList());
            newList.addAll(currentList);

            ChannelAbstract.getConnectionList().set(serverConnection, newList, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract IListenerList newListenerList();

    protected final Object onPacketSend(Object receiver, Object packet, Cancellable cancellable) {
        return this.getIPacketListener().onPacketSend(receiver, packet, cancellable);
    }

    protected final Object onPacketReceive(Object sender, Object packet, Cancellable cancellable) {
        return this.getIPacketListener().onPacketReceive(sender, packet, cancellable);
    }

    Executor getAddChannelExecutor() {
        return this.addChannelExecutor;
    }

    Executor getRemoveChannelExecutor() {
        return this.removeChannelExecutor;
    }

    IPacketListener getIPacketListener() {
        return this.iPacketListener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelAbstract that = (ChannelAbstract) o;
        return Objects.equals(this.getAddChannelExecutor(), that.getAddChannelExecutor()) &&
                Objects.equals(this.getRemoveChannelExecutor(), that.getRemoveChannelExecutor()) &&
                Objects.equals(this.iPacketListener, that.iPacketListener);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getAddChannelExecutor(), this.getRemoveChannelExecutor(), this.iPacketListener);
    }

    @Override
    public String toString() {
        return "ChannelAbstract{" +
                "                            addChannelExecutor=" + this.addChannelExecutor +
                ",                             removeChannelExecutor=" + this.removeChannelExecutor +
                ",                             iPacketListener=" + this.iPacketListener +
                '}';
    }

    interface IListenerList<E> extends List<E> {
    }

    interface IChannelHandler {
    }

    interface IChannelWrapper {
    }
}
