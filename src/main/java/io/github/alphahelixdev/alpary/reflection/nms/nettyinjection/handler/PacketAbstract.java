package io.github.alphahelixdev.alpary.reflection.nms.nettyinjection.handler;

import io.github.alphahelixdev.alpary.reflection.nms.nettyinjection.channel.ChannelWrapper;
import io.github.alphahelixdev.alpary.utils.Utils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Getter
@EqualsAndHashCode
public class PacketAbstract {
	
	private Player player;
	private ChannelWrapper channel;
	@Setter
	private Object packet;
	private Cancellable cancellable;
	
	public PacketAbstract(Object packet, Cancellable cancellable, Player player) {
		this.player = player;
		this.packet = packet;
		this.cancellable = cancellable;
	}
	
	public PacketAbstract(Object packet, Cancellable cancellable, ChannelWrapper channel) {
		this.channel = channel;
		this.packet = packet;
		this.cancellable = cancellable;
	}
	
	public void setPacketValue(String field, Object value) {
        Utils.nms().getDeclaredField(field, packet.getClass()).set(this.packet, value, true);
	}
	
	public void setPacketValueSilent(String field, Object value) {
        Utils.nms().getDeclaredField(field, packet.getClass()).set(this.packet, value, false);
	}
	
	public void setPacketValue(int index, Object value) {
        Utils.nms().getDeclaredFields(packet.getClass()).get(index).set(packet, value, true);
	}
	
	public void setPacketValueSilent(int index, Object value) {
        Utils.nms().getDeclaredFields(packet.getClass()).get(index).set(packet, value, false);
	}
	
	public Object getPacketValue(String field) {
        return Utils.nms().getDeclaredField(field, packet.getClass()).get(this.packet, true);
	}
	
	public Object getPacketValueSilent(String field) {
        return Utils.nms().getDeclaredField(field, packet.getClass()).get(this.packet, false);
	}
	
	public Object getPacketValue(int index) {
        return Utils.nms().getDeclaredFields(packet.getClass()).get(index).get(packet, true);
	}
	
	public Object getPacketValueSilent(int index) {
        return Utils.nms().getDeclaredFields(packet.getClass()).get(index).get(packet, false);
	}
	
	public boolean isCancelled() {
		return this.cancellable.isCancelled();
	}
	
	public PacketAbstract setCancelled(boolean b) {
		this.cancellable.setCancelled(b);
		return this;
	}
	
	public String toString() {
		return "Packet{ " + (this.getClass().equals(SentPacket.class) ? "[> OUT >]" : "[< IN <]") + " " + this.getPacketName() + " " + (this.hasPlayer() ? this.getPlayer().getName() : (this.hasChannel() ? this.getChannel().channel() : "#server#")) + " }";
	}
	
	public String getPacketName() {
		return getPacket().getClass().getSimpleName();
	}
	
	public boolean hasPlayer() {
		return getPlayer() != null;
	}
	
	public boolean hasChannel() {
		return getChannel() != null;
	}
}
