package de.alphahelix.alphalibary.netty;

import de.alphahelix.alphalibary.netty.channel.ChannelAbstract;
import de.alphahelix.alphalibary.netty.channel.INCChannel;
import org.bukkit.entity.Player;

public class ChannelInjector {

    private ChannelAbstract channel;

    public boolean inject(IPacketListener iPacketListener) {
        channel = new INCChannel(iPacketListener);
        return true;
    }

    public void addChannel(Player p) {
        this.channel.addChannel(p);
    }

    public void removeChannel(Player p) {
        this.channel.removeChannel(p);
    }

    public void addServerChannel() {
        this.channel.addServerChannel();
    }
}
