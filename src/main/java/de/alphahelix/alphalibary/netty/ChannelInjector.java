package de.alphahelix.alphalibary.netty;

import com.google.common.base.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelInjector that = (ChannelInjector) o;
        return Objects.equal(channel, that.channel);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(channel);
    }

    @Override
    public String toString() {
        return "ChannelInjector{" +
                "channel=" + channel +
                '}';
    }
}
