package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import io.github.alphahelixdev.helius.utils.MathUtil;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class RelEntityMovePacket implements IPacket {
	
	private static final SaveConstructor PACKET = NMSUtil.getReflections().getDeclaredConstructor(
			Utils.nms().getNMSClass("PacketPlayOutEntity$PacketPlayOutRelEntityMove"), int.class, long.class,
			long.class, long.class, boolean.class);
	
	private int entityID;
	private double x, y, z;
	private boolean onGround;
	
	private static SaveConstructor getPacket() {
		return RelEntityMovePacket.PACKET;
	}
	
	@Override
	public Object getPacket(boolean stackTrace) {
		return RelEntityMovePacket.getPacket().newInstance(stackTrace, this.getEntityID(),
				MathUtil.toDelta(this.getX()), MathUtil.toDelta(this.getY()), MathUtil.toDelta(this.getZ()),
				this.isOnGround());
	}
}
