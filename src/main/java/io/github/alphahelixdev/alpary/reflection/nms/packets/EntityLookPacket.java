package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.helix.reflection.SaveConstructor;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class EntityLookPacket implements IPacket {

    private static final SaveConstructor PACKET = Utils.nms().getDeclaredConstructor(
            Utils.nms().getNMSClass("PacketPlayOutEntity$PacketPlayOutEntityLook"), int.class, byte.class,
            byte.class, boolean.class);

    private int entityID;
    private float yaw, pitch;
    private boolean onGround;

    private static SaveConstructor getPacket() {
        return EntityLookPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return EntityLookPacket.getPacket().newInstance(stackTrace, this.getEntityID(), Utils.math().toAngle(this.getYaw()),
                Utils.math().toAngle(this.getPitch()), this.isOnGround());
    }
}
