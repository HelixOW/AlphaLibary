package de.alphahelix.alphalibary.reflection.nms.packets;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.core.utils.abstracts.AbstractReflectionUtil;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.BlockPos;


public class PPOOpenSignEditor implements IPacket {
	
	private static final AbstractReflectionUtil.SaveConstructor PACKET =
			ReflectionUtil.getDeclaredConstructor("PacketPlayOutOpenSignEditor",
					ReflectionUtil.getNmsClass("BlockPosition"));
	
	private BlockPos pos;
	
	public PPOOpenSignEditor(BlockPos pos) {
		this.pos = pos;
	}
	
	@Override
	public Object getPacket(boolean stackTrace) {
		return PACKET.newInstance(stackTrace, ReflectionUtil.toBlockPosition(pos));
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getPos());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		PPOOpenSignEditor that = (PPOOpenSignEditor) o;
		return Objects.equal(getPos(), that.getPos());
	}
	
	@Override
	public String toString() {
		return "PPOOpenSignEditor{" +
				"pos=" + pos +
				'}';
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public PPOOpenSignEditor setPos(BlockPos pos) {
		this.pos = pos;
		return this;
	}
}
