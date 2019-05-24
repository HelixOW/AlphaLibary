package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.reflection.nms.enums.AnimationType;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.helix.reflection.SaveConstructor;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class AnimationPacket implements IPacket {
	
	private static final SaveConstructor PACKET =
			Alpary.getInstance().reflections().getDeclaredConstructor(Utils.nms().getNMSClass("PacketPlayOutAnimation"),
					Utils.nms().getNMSClass("Entity"), int.class);
	
	private Object entity;
	private int animationType;
	
	public AnimationPacket(Object entity, AnimationType type) {
		this(entity, type.ordinal());
	}
	
	@Override
	public Object getPacket(boolean stackTrace) {
		return getPacket().newInstance(stackTrace, this.getEntity(), this.getAnimationType());
	}
	
	private SaveConstructor getPacket() {
		return PACKET;
	}
}
