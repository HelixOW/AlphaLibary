package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.helix.reflection.SaveConstructor;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class NamedEntitySpawnPacket implements IPacket {

    private static final SaveConstructor PACKET = Utils.nms().getDeclaredConstructor(
            Utils.nms().getNMSClass("PacketPlayOutNamedEntitySpawn"),
            Utils.nms().getNMSClass("EntityHuman"));

    private Object entityHuman;

    private static SaveConstructor getPacket() {
        return NamedEntitySpawnPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return NamedEntitySpawnPacket.getPacket().newInstance(stackTrace, this.getEntityHuman());
    }
}
