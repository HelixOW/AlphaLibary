package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.reflection.nms.BlockPos;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.helix.reflection.SaveConstructor;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class SpawnEntityPacket implements IPacket {

    private static final SaveConstructor PACKET_OPT_1 = Utils.nms().getDeclaredConstructor(
			Utils.nms().getNMSClass("PacketPlayOutSpawnEntity"), Utils.nms().getNMSClass("Entity"),
			int.class, int.class);
    private static final SaveConstructor PACKET_OPT_2 = Utils.nms().getDeclaredConstructor(
			Utils.nms().getNMSClass("PacketPlayOutSpawnEntity"), Utils.nms().getNMSClass("Entity"),
			int.class, int.class, Utils.nms().getNMSClass("BlockPosition"));
	
	@NonNull
	private Object entity;
	@NonNull
	private int type;
	@NonNull
	private int metaData;
	private BlockPos pos;
	
	@Override
	public Object getPacket(boolean stackTrace) {
		if(this.getPos() == null)
			return PACKET_OPT_1.newInstance(stackTrace, this.getEntity(), this.getType(),
					this.getMetaData());
		return PACKET_OPT_2.newInstance(stackTrace, this.getEntity(), this.getType(),
				this.getMetaData(), Utils.nms().toBlockPosition(this.getPos()));
	}
}
