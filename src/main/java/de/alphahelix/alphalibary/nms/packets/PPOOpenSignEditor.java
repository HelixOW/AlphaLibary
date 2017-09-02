package de.alphahelix.alphalibary.nms.packets;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.nms.BlockPos;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public class PPOOpenSignEditor implements IPacket {

    private static final ReflectionUtil.SaveConstructor PACKET =
            ReflectionUtil.getDeclaredConstructor("PacketPlayOutOpenSignEditor",
                    ReflectionUtil.getNmsClass("BlockPosition"));

    private BlockPos pos;

    public PPOOpenSignEditor(BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }

    public PPOOpenSignEditor setPos(BlockPos pos) {
        this.pos = pos;
        return this;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return PACKET.newInstance(stackTrace, ReflectionUtil.toBlockPosition(pos));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PPOOpenSignEditor that = (PPOOpenSignEditor) o;
        return Objects.equal(getPos(), that.getPos());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPos());
    }

    @Override
    public String toString() {
        return "PPOOpenSignEditor{" +
                "pos=" + pos +
                '}';
    }
}
