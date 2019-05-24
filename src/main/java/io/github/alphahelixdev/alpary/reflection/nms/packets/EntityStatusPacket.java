package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.reflection.nms.enums.EntityStatus;
import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class EntityStatusPacket implements IPacket {

    private static final SaveConstructor PACKET = NMSUtil.getReflections().getDeclaredConstructor(
            Utils.nms().getNMSClass("PacketPlayOutEntityStatus"),
            Utils.nms().getNMSClass("Entity"), byte.class);

    private Object entity;
    private EntityStatus status;

    private static SaveConstructor getPacket() {
        return EntityStatusPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return EntityStatusPacket.getPacket().newInstance(stackTrace, this.getEntity(), this.getStatus().getId());
    }

}
