package io.github.alphahelixdev.alpary.reflection.nms.netty;

import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.reflection.nms.netty.channel.ChannelWrapper;
import io.github.alphahelixdev.alpary.reflection.nms.netty.handler.PacketHandler;
import io.github.alphahelixdev.alpary.reflection.nms.netty.handler.ReceivedPacket;
import io.github.alphahelixdev.alpary.reflection.nms.netty.handler.SentPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;


public class PacketListener implements IPacketListener, Listener {

    protected boolean injected = false;
    private ChannelInjector channelInjector;

    public static boolean addPacketHandler(PacketHandler handler) {
        return PacketHandler.addHandler(handler);
    }

    public static boolean removePacketHandler(PacketHandler handler) {
        return PacketHandler.removeHandler(handler);
    }

    public void enable() {
        Bukkit.getPluginManager().registerEvents(this, Alpary.getInstance());

        for (Player player : Bukkit.getOnlinePlayers()) {
            this.getChannelInjector().addChannel(player);
        }
    }

    public void disable() {
        if (!this.isInjected()) return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            this.getChannelInjector().removeChannel(player);
        }

        while (!PacketHandler.getHandlers().isEmpty()) {
            PacketHandler.removeHandler(PacketHandler.getHandlers().get(0));
        }
    }

    public void load() {
        channelInjector = new ChannelInjector();
        this.getChannelInjector().inject(this);

        injected = true;
        this.getChannelInjector().addServerChannel();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        this.getChannelInjector().addChannel(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        this.getChannelInjector().removeChannel(e.getPlayer());
    }

    @Override
    public Object onPacketSend(Object receiver, Object packet, org.bukkit.event.Cancellable cancellable) {
        SentPacket sentPacket;

        if (receiver instanceof Player)
            sentPacket = new SentPacket(packet, cancellable, (Player) receiver);
        else
            sentPacket = new SentPacket(packet, cancellable, (ChannelWrapper) receiver);

        PacketHandler.notifyHandlers(sentPacket);

        if (sentPacket.getPacket() != null)
            return sentPacket.getPacket();

        return packet;
    }

    @Override
    public Object onPacketReceive(Object sender, Object packet, org.bukkit.event.Cancellable cancellable) {
        ReceivedPacket receivedPacket;

        if (sender instanceof Player)
            receivedPacket = new ReceivedPacket(packet, cancellable, (Player) sender);
        else
            receivedPacket = new ReceivedPacket(packet, cancellable, (ChannelWrapper) sender);

        PacketHandler.notifyHandlers(receivedPacket);

        if (receivedPacket.getPacket() != null)
            return receivedPacket.getPacket();

        return packet;
    }

    public boolean isInjected() {
        return this.injected;
    }

    public ChannelInjector getChannelInjector() {
        return this.channelInjector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PacketListener that = (PacketListener) o;
        return this.isInjected() == that.isInjected() &&
                Objects.equals(this.getChannelInjector(), that.getChannelInjector());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.isInjected(), this.getChannelInjector());
    }

    @Override
    public String toString() {
        return "PacketListener{" +
                "                            injected=" + this.injected +
                ",                             channelInjector=" + this.channelInjector +
                '}';
    }
}
