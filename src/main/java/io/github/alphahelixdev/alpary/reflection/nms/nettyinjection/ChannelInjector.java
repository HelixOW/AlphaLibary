package io.github.alphahelixdev.alpary.reflection.nms.nettyinjection;

import io.github.alphahelixdev.alpary.reflection.nms.nettyinjection.channel.ChannelAbstract;
import io.github.alphahelixdev.alpary.reflection.nms.nettyinjection.channel.INCChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Player;

@Getter
@EqualsAndHashCode
@ToString
public class ChannelInjector {
	
	private ChannelAbstract channel;
	
	public boolean inject(INettyInjector iNettyInjector) {
		try {
			Class.forName("io.netty.channel.Channel");
			this.channel = new INCChannel(iNettyInjector);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
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
