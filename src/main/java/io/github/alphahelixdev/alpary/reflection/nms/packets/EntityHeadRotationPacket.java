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
public class EntityHeadRotationPacket implements IPacket {

    private static final SaveConstructor PACKET = NMSUtil.getReflections().getDeclaredConstructor(
            Utils.nms().getNMSClass("PacketPlayOutEntityHeadRotation"), Utils.nms().getNMSClass("Entity"),
            byte.class);

    private Object entity;
    private float yaw;

    public static SaveConstructor getPacket() {
        return EntityHeadRotationPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return EntityHeadRotationPacket.getPacket().newInstance(stackTrace, this.getEntity(),
                MathUtil.toAngle(this.getYaw()));
    }
}
