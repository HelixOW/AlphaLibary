package de.alphahelix.alphalibary.netty.handler;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PacketHandler {

    private static final List<PacketHandler> handlers = new ArrayList<>();

    private boolean hasSendOptions;
    private boolean forcePlayerSend;
    private boolean forceServerSend;

    private boolean hasReceiveOptions;
    private boolean forcePlayerReceive;
    private boolean forceServerReceive;

    public static boolean addHandler(PacketHandler handler) {
        boolean b = handlers.contains(handler);
        if (!b) {
            try {
                PacketOptions options = handler.getClass().getMethod("onSend", SentPacket.class).getAnnotation(PacketOptions.class);
                if (options != null) {
                    handler.hasSendOptions = true;
                    if (options.forcePlayer() && options.forceServer()) {
                        throw new IllegalArgumentException("Cannot force player and server packets at the same time!");
                    }
                    if (options.forcePlayer()) {
                        handler.forcePlayerSend = true;
                    } else if (options.forceServer()) {
                        handler.forceServerSend = true;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to register handler (onSend)", e);
            }
            try {
                PacketOptions options = handler.getClass().getMethod("onReceive", ReceivedPacket.class).getAnnotation(PacketOptions.class);
                if (options != null) {
                    handler.hasReceiveOptions = true;
                    if (options.forcePlayer() && options.forceServer()) {
                        throw new IllegalArgumentException("Cannot force player and server packets at the same time!");
                    }
                    if (options.forcePlayer()) {
                        handler.forcePlayerReceive = true;
                    } else if (options.forceServer()) {
                        handler.forceServerReceive = true;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to register handler (onReceive)", e);
            }
        }
        handlers.add(handler);
        return !b;
    }

    public static boolean removeHandler(PacketHandler handler) {
        return handlers.remove(handler);
    }

    public static void notifyHandlers(SentPacket packet) {
        for (PacketHandler handler : getHandlers()) {
            try {
                if (handler.hasSendOptions) {
                    if (handler.forcePlayerSend) {
                        if (!packet.hasPlayer()) {
                            continue;
                        }
                    } else if (handler.forceServerSend) {
                        if (!packet.hasChannel()) {
                            continue;
                        }
                    }
                }
                handler.onSend(packet);
            } catch (Exception e) {
                System.err.println("[PacketListenerAPI] An exceptions occured while trying to execute 'onSend': " + e.getMessage());
                e.printStackTrace(System.err);
            }
        }
    }

    public static void notifyHandlers(ReceivedPacket packet) {
        for (PacketHandler handler : getHandlers()) {
            try {
                if (handler.hasReceiveOptions) {
                    if (handler.forcePlayerReceive) {
                        if (!packet.hasPlayer()) {
                            continue;
                        }
                    } else if (handler.forceServerReceive) {
                        if (!packet.hasChannel()) {
                            continue;
                        }
                    }
                }
                handler.onReceive(packet);
            } catch (Exception e) {
                System.err.println("[PacketListenerAPI] An exceptions occured while trying to execute 'onReceive': " + e.getMessage());
                e.printStackTrace(System.err);
            }
        }
    }

    public static List<PacketHandler> getHandlers() {
        return new ArrayList<>(handlers);
    }

    // Sending methods
    public void sendPacket(Player p, Object packet) {
        if (p == null || packet == null) {
            throw new NullPointerException();
        }
        ReflectionUtil.sendPacket(p, packet);
    }

    // //////////////////////////////////////////////////

    public void onSend(SentPacket packet) {
    }

    public void onReceive(ReceivedPacket packet) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PacketHandler that = (PacketHandler) o;
        return hasSendOptions == that.hasSendOptions &&
                forcePlayerSend == that.forcePlayerSend &&
                forceServerSend == that.forceServerSend &&
                hasReceiveOptions == that.hasReceiveOptions &&
                forcePlayerReceive == that.forcePlayerReceive &&
                forceServerReceive == that.forceServerReceive;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hasSendOptions, forcePlayerSend, forceServerSend, hasReceiveOptions, forcePlayerReceive, forceServerReceive);
    }

    @Override
    public String toString() {
        return "PacketHandler{" +
                "hasSendOptions=" + hasSendOptions +
                ", forcePlayerSend=" + forcePlayerSend +
                ", forceServerSend=" + forceServerSend +
                ", hasReceiveOptions=" + hasReceiveOptions +
                ", forcePlayerReceive=" + forcePlayerReceive +
                ", forceServerReceive=" + forceServerReceive +
                '}';
    }
}
