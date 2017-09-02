package de.alphahelix.alphalibary.netty;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.netty.channel.ChannelWrapper;
import de.alphahelix.alphalibary.netty.handler.PacketHandler;
import de.alphahelix.alphalibary.netty.handler.ReceivedPacket;
import de.alphahelix.alphalibary.netty.handler.SentPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class PacketListenerAPI implements IPacketListener, Listener {

    protected boolean injected = false;
    private ChannelInjector channelInjector;

    public static boolean addPacketHandler(PacketHandler handler) {
        return PacketHandler.addHandler(handler);
    }

    public static boolean removePacketHandler(PacketHandler handler) {
        return PacketHandler.removeHandler(handler);
    }

    /**
     * Call onLoad
     */
    public void load() {
        channelInjector = new ChannelInjector();

        if (injected = channelInjector.inject(this)) {
            channelInjector.addServerChannel();
        }
    }

    /**
     * Call onEnable
     */
    public void init() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            channelInjector.addChannel(player);
        }
    }

    /**
     * Call onDisable
     */
    public void disable() {
        if (!injected) return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            channelInjector.removeChannel(player);
        }

        while (!PacketHandler.getHandlers().isEmpty()) {
            PacketHandler.removeHandler(PacketHandler.getHandlers().get(0));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        channelInjector.addChannel(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        channelInjector.removeChannel(e.getPlayer());
    }

    @Override
    public Object onPacketReceive(Object sender, Object packet, org.bukkit.event.Cancellable cancellable) {
        ReceivedPacket receivedPacket;

        if (sender instanceof Player) {
            receivedPacket = new ReceivedPacket(packet, cancellable, (Player) sender);
        } else {
            receivedPacket = new ReceivedPacket(packet, cancellable, (ChannelWrapper) sender);
        }
        PacketHandler.notifyHandlers(receivedPacket);
        if (receivedPacket.getPacket() != null) {
            return receivedPacket.getPacket();
        }
        return packet;
    }

    @Override
    public Object onPacketSend(Object receiver, Object packet, org.bukkit.event.Cancellable cancellable) {
        SentPacket sentPacket;

        if (receiver instanceof Player) {
            sentPacket = new SentPacket(packet, cancellable, (Player) receiver);
        } else {
            sentPacket = new SentPacket(packet, cancellable, (ChannelWrapper) receiver);
        }
        PacketHandler.notifyHandlers(sentPacket);
        if (sentPacket.getPacket() != null) {
            return sentPacket.getPacket();
        }
        return packet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PacketListenerAPI that = (PacketListenerAPI) o;
        return injected == that.injected &&
                Objects.equal(channelInjector, that.channelInjector);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(injected, channelInjector);
    }

    @Override
    public String toString() {
        return "PacketListenerAPI{" +
                "injected=" + injected +
                ", channelInjector=" + channelInjector +
                '}';
    }
}
