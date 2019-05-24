package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class SpawnEntityLivingPacket implements IPacket {

    private static final SaveConstructor PACKET = NMSUtil.getReflections().getDeclaredConstructor(
            Utils.nms().getNMSClass("PacketPlayOutSpawnEntityLiving"),
            Utils.nms().getNMSClass("EntityLiving"));

    private Object entity;

    private static SaveConstructor getPacket() {
        return SpawnEntityLivingPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return SpawnEntityLivingPacket.getPacket().newInstance(stackTrace, this.getEntity());
    }
}
