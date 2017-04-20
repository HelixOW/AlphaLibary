package de.alphahelix.alphalibary.netty.handler;

import de.alphahelix.alphalibary.netty.channel.ChannelWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class ReceivedPacket extends PacketAbstract {
    public ReceivedPacket(Object packet, Cancellable cancellable, Player player) {
        super(packet, cancellable, player);
    }

    public ReceivedPacket(Object packet, Cancellable cancellable, ChannelWrapper channelWrapper) {
        super(packet, cancellable, channelWrapper);
    }
}
