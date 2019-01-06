package io.github.alphahelixdev.alpary.reflection.nms.netty;

import io.github.alphahelixdev.alpary.reflection.nms.netty.channel.ChannelAbstract;
import io.github.alphahelixdev.alpary.reflection.nms.netty.channel.INCChannel;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ChannelInjector {

    private ChannelAbstract channel;

    public void inject(IPacketListener iPacketListener) {
        channel = new INCChannel(iPacketListener);
    }

    public void addChannel(Player p) {
        this.getChannel().addChannel(p);
    }

    public void removeChannel(Player p) {
        this.getChannel().removeChannel(p);
    }

    public void addServerChannel() {
        this.getChannel().addServerChannel();
    }

    public ChannelAbstract getChannel() {
        return this.channel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelInjector that = (ChannelInjector) o;
        return Objects.equals(this.getChannel(), that.getChannel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getChannel());
    }

    @Override
    public String toString() {
        return "ChannelInjector{" +
                "                            channel=" + this.channel +
                '}';
    }
}
