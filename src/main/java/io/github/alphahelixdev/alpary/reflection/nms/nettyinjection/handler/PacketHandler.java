package io.github.alphahelixdev.alpary.reflection.nms.nettyinjection.handler;

import io.github.alphahelixdev.alpary.utils.Utils;
import lombok.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public abstract class PacketHandler {

    private static final List<PacketHandler> HANDLERS = new ArrayList<>();
    private boolean hasSendOptions, forceSendPlayer, forceSendServer, hasReceiveOptions, forcePlayerReceive,
            forceServerReceive;
    @NonNull
    private JavaPlugin plugin;

    public static boolean addHandler(PacketHandler handler) {
        boolean b = HANDLERS.contains(handler);

        if (!b) {
            PacketOptions opt =
                    Utils.nms().getMethod("onSend", handler.getClass(), SentPacket.class).asNormal()
                            .getAnnotation(PacketOptions.class);

            if (opt != null) {
                handler.hasSendOptions = true;

                if (opt.forcePlayer() && opt.forceServer())
                    throw new IllegalArgumentException("Can't force player and server packet at the same time");

                if (opt.forcePlayer())
                    handler.forceSendPlayer = true;
                else if (opt.forceServer())
                    handler.forceSendServer = true;
            }

            opt = Utils.nms().getMethod("onReceive", handler.getClass(), ReceivedPacket.class)
                    .asNormal().getAnnotation(PacketOptions.class);

            if (opt != null) {
                handler.hasReceiveOptions = true;

                if (opt.forcePlayer() && opt.forceServer())
                    throw new IllegalArgumentException("Can't force player and server packet at the same time");

                if (opt.forcePlayer())
                    handler.forcePlayerReceive = true;
                else if (opt.forceServer())
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
        HANDLERS.stream().filter(PacketHandler::isHasSendOptions)
                .filter(packetHandler -> (packetHandler.isForceSendPlayer() && packet.hasPlayer())
                        || (packetHandler.isForceSendServer() && packet.hasChannel()))
                .forEach(packetHandler -> packetHandler.onSend(packet));
    }

    public static void notifyHandlers(ReceivedPacket packet) {
        HANDLERS.stream().filter(PacketHandler::isHasReceiveOptions)
                .filter(packetHandler -> (packetHandler.isForcePlayerReceive() && packet.hasPlayer())
                        || (packetHandler.isForceServerReceive() && packet.hasChannel()))
                .forEach(packetHandler -> packetHandler.onReceive(packet));
    }

    public static List<PacketHandler> getHandlersForPlugin(JavaPlugin plugin) {
        if (plugin == null)
            return HANDLERS;

        return HANDLERS.stream().filter(packetHandler -> packetHandler.getPlugin().equals(plugin))
                .collect(Collectors.toList());
    }

    public static List<PacketHandler> handlers() {
        return HANDLERS;
    }

    public abstract void onSend(SentPacket var1);

    public abstract void onReceive(ReceivedPacket var1);
}
