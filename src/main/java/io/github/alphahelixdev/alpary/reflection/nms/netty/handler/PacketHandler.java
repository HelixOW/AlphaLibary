package io.github.alphahelixdev.alpary.reflection.nms.netty.handler;

import io.github.alphahelixdev.alpary.utils.Utils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PacketHandler {

    private static final List<PacketHandler> HANDLERS = new ArrayList<>();

    private boolean hasSendOptions;
    private boolean forcePlayerSend;
    private boolean forceServerSend;

    private boolean hasReceiveOptions;
    private boolean forcePlayerReceive;
    private boolean forceServerReceive;

    public static boolean addHandler(PacketHandler handler) {
        boolean b = PacketHandler.getHandlers().contains(handler);
        if (!b) {
            try {
                PacketOptions options = handler.getClass().getMethod("onSend", SentPacket.class)
                        .getAnnotation(PacketOptions.class);
                if (options != null) {
                    handler.setHasSendOptions(true);
                    if (options.forcePlayer() && options.forceServer())
                        throw new IllegalArgumentException("Cannot force player and server packets at the same time!");

                    if (options.forcePlayer())
                        handler.setForcePlayerSend(true);
                    else if (options.forceServer())
                        handler.setForceServerSend(true);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to register handler (onSend)", e);
            }

            try {
                PacketOptions options = handler.getClass().getMethod("onReceive", ReceivedPacket.class)
                        .getAnnotation(PacketOptions.class);
                if (options != null) {
                    handler.setHasReceiveOptions(true);
                    if (options.forcePlayer() && options.forceServer())
                        throw new IllegalArgumentException("Cannot force player and server packets at the same time!");

                    if (options.forcePlayer())
                        handler.setForcePlayerReceive(true);
                    else if (options.forceServer())
                        handler.setForceServerReceive(true);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to register handler (onReceive)", e);
            }
        }
        PacketHandler.getHandlers().add(handler);
        return !b;
    }

    public static boolean removeHandler(PacketHandler handler) {
        return PacketHandler.getHandlers().remove(handler);
    }

    public static void notifyHandlers(SentPacket packet) {
        for (PacketHandler handler : getHandlers()) {
            try {
                if (handler.isHasSendOptions()) {
                    if (handler.isForcePlayerSend()) {
                        if (!packet.hasPlayer())
                            continue;
                    } else if (handler.isForceServerSend()) {
                        if (!packet.hasChannel())
                            continue;
                    }
                }

                handler.onSend(packet);
            } catch (Exception e) {
                throw new RuntimeException("An exception occured while trying to execute 'onSend': " + e.getMessage());
            }
        }
    }

    public static void notifyHandlers(ReceivedPacket packet) {
        for (PacketHandler handler : getHandlers()) {
            try {
                if (handler.isHasReceiveOptions()) {
                    if (handler.isForcePlayerReceive()) {
                        if (!packet.hasPlayer())
                            continue;
                    } else if (handler.isForceServerReceive()) {
                        if (!packet.hasChannel())
                            continue;
                    }
                }
                handler.onReceive(packet);
            } catch (Exception e) {
                throw new RuntimeException("An exception occured while trying to execute 'onReceive': " +
                        e.getMessage());
            }
        }
    }

    public static List<PacketHandler> getHandlers() {
        return HANDLERS;
    }

    public void sendPacket(Player p, Object packet) {
        if (p == null || packet == null)
            throw new NullPointerException();

        Utils.nms().sendPacket(p, packet);
    }

    public void onSend(SentPacket packet) {
    }

    public void onReceive(ReceivedPacket packet) {
    }

    public boolean isHasSendOptions() {
        return this.hasSendOptions;
    }

    public PacketHandler setHasSendOptions(boolean hasSendOptions) {
        this.hasSendOptions = hasSendOptions;
        return this;
    }

    public boolean isForcePlayerSend() {
        return this.forcePlayerSend;
    }

    public PacketHandler setForcePlayerSend(boolean forcePlayerSend) {
        this.forcePlayerSend = forcePlayerSend;
        return this;
    }

    public boolean isForceServerSend() {
        return this.forceServerSend;
    }

    public PacketHandler setForceServerSend(boolean forceServerSend) {
        this.forceServerSend = forceServerSend;
        return this;
    }

    public boolean isHasReceiveOptions() {
        return this.hasReceiveOptions;
    }

    public PacketHandler setHasReceiveOptions(boolean hasReceiveOptions) {
        this.hasReceiveOptions = hasReceiveOptions;
        return this;
    }

    public boolean isForcePlayerReceive() {
        return this.forcePlayerReceive;
    }

    public PacketHandler setForcePlayerReceive(boolean forcePlayerReceive) {
        this.forcePlayerReceive = forcePlayerReceive;
        return this;
    }

    public boolean isForceServerReceive() {
        return this.forceServerReceive;
    }

    public PacketHandler setForceServerReceive(boolean forceServerReceive) {
        this.forceServerReceive = forceServerReceive;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PacketHandler that = (PacketHandler) o;
        return this.isHasSendOptions() == that.isHasSendOptions() &&
                this.isForcePlayerSend() == that.isForcePlayerSend() &&
                this.isForceServerSend() == that.isForceServerSend() &&
                this.isHasReceiveOptions() == that.isHasReceiveOptions() &&
                this.isForcePlayerReceive() == that.isForcePlayerReceive() &&
                this.isForceServerReceive() == that.isForceServerReceive();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.isHasSendOptions(), this.isForcePlayerSend(), this.isForceServerSend(), this.isHasReceiveOptions(), this.isForcePlayerReceive(), this.isForceServerReceive());
    }


    @Override
    public String toString() {
        return "PacketHandler{" +
                "                            hasSendOptions=" + this.hasSendOptions +
                ",                             forcePlayerSend=" + this.forcePlayerSend +
                ",                             forceServerSend=" + this.forceServerSend +
                ",                             hasReceiveOptions=" + this.hasReceiveOptions +
                ",                             forcePlayerReceive=" + this.forcePlayerReceive +
                ",                             forceServerReceive=" + this.forceServerReceive +
                '}';
    }
}
