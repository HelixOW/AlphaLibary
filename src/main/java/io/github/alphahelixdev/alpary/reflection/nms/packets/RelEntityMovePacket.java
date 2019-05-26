package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.helix.reflection.SaveConstructor;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class RelEntityMovePacket implements IPacket {

    private static final SaveConstructor PACKET = Utils.nms().getDeclaredConstructor(
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
                Utils.math().toDelta(this.getX()), Utils.math().toDelta(this.getY()), Utils.math().toDelta(this.getZ()),
				this.isOnGround());
	}
}
