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
public class MountPacket implements IPacket {

    private static final SaveConstructor PACKET = NMSUtil.getReflections().getDeclaredConstructor(
            Utils.nms().getNMSClass("PacketPlayOutMount"), Utils.nms().getNMSClass("Entity"));

    private Object entity;

    public static SaveConstructor getPacket() {
        return MountPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return MountPacket.getPacket().newInstance(stackTrace, this.getEntity());
    }
}
