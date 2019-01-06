package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.reflection.nms.BlockPos;
import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;

import java.util.Objects;

public class OpenSignEditorPacket implements IPacket {

    private static final SaveConstructor PACKET =
            NMSUtil.getReflections().getDeclaredConstructor(
                    Utils.nms().getNMSClass("PacketPlayOutOpenSignEditor"),
                    Utils.nms().getNMSClass("BlockPosition"));

    private BlockPos pos;

    public OpenSignEditorPacket(BlockPos pos) {
        this.pos = pos;
    }

    private static SaveConstructor getPacket() {
        return OpenSignEditorPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return OpenSignEditorPacket.getPacket().newInstance(stackTrace, Utils.nms().toBlockPosition(this.getPos()));
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public OpenSignEditorPacket setPos(BlockPos pos) {
        this.pos = pos;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenSignEditorPacket that = (OpenSignEditorPacket) o;
        return Objects.equals(this.getPos(), that.getPos());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPos());
    }

    @Override
    public String toString() {
        return "OpenSignEditorPacket{" +
                "                            pos=" + this.pos +
                '}';
    }
}
