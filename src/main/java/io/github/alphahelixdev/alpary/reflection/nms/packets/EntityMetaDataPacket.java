package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.helix.reflection.SaveConstructor;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class EntityMetaDataPacket implements IPacket {

    private static final SaveConstructor PACKET = Utils.nms().getDeclaredConstructor(
            Utils.nms().getNMSClass("PacketPlayOutEntityMetadata"), int.class,
            Utils.nms().getNMSClass("DataWatcher"), boolean.class);
	
	@NonNull
	private int entityID;
	@NonNull
	private Object dataWatcher;
    private boolean updateItem = true; //not sure about that

    private static SaveConstructor getPacket() {
        return EntityMetaDataPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return EntityMetaDataPacket.getPacket().newInstance(stackTrace, this.getEntityID(), this.getDataWatcher(),
		        this.isUpdateItem());
    }
}
