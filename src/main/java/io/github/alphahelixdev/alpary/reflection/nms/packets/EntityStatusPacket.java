package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.reflection.nms.enums.REntityStatus;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.helix.reflection.SaveConstructor;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class EntityStatusPacket implements IPacket {

    private static final SaveConstructor PACKET = Utils.nms().getDeclaredConstructor(
            Utils.nms().getNMSClass("PacketPlayOutEntityStatus"),
            Utils.nms().getNMSClass("Entity"), byte.class);

    private Object entity;
    private REntityStatus status;

    private static SaveConstructor getPacket() {
        return EntityStatusPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return EntityStatusPacket.getPacket().newInstance(stackTrace, this.getEntity(), this.getStatus().getId());
    }

}
