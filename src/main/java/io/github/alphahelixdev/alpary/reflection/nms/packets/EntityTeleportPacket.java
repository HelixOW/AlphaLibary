package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.helix.reflection.SaveConstructor;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class EntityTeleportPacket implements IPacket {

    private static final SaveConstructor PACKET = Utils.nms().getDeclaredConstructor(
            Utils.nms().getNMSClass("PacketPlayOutEntityTeleport"), Utils.nms().getNMSClass("Entity"));

    private Object entity;

    private static SaveConstructor getPacket() {
        return EntityTeleportPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return EntityTeleportPacket.getPacket().newInstance(stackTrace, this.getEntity());
    }
}
