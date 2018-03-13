package de.alphahelix.alphalibary.reflection.nms.netty;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.reflection.nms.netty.channel.ChannelAbstract;
import de.alphahelix.alphalibary.reflection.nms.netty.channel.INCChannel;
import org.bukkit.entity.Player;


public class ChannelInjector {
	
	private ChannelAbstract channel;
	
	public void inject(IPacketListener iPacketListener) {
		channel = new INCChannel(iPacketListener);
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
	public int hashCode() {
		return Objects.hashCode(channel);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		ChannelInjector that = (ChannelInjector) o;
		return Objects.equal(channel, that.channel);
	}
	
	@Override
	public String toString() {
		return "ChannelInjector{" +
				"channel=" + channel +
				'}';
	}
}
