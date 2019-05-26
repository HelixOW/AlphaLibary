package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.reflection.nms.BlockPos;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.helix.reflection.SaveConstructor;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class OpenSignEditorPacket implements IPacket {

    private static final SaveConstructor PACKET =
            Utils.nms().getDeclaredConstructor(
                    Utils.nms().getNMSClass("PacketPlayOutOpenSignEditor"),
                    Utils.nms().getNMSClass("BlockPosition"));

    private BlockPos pos;

    private static SaveConstructor getPacket() {
        return OpenSignEditorPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return OpenSignEditorPacket.getPacket().newInstance(stackTrace, Utils.nms().toBlockPosition(this.getPos()));
    }
}
