package de.alphahelix.alphalibary.reflection.nms.packets;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.core.utils.abstracts.AbstractReflectionUtil;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.BlockPos;


public class PPOSpawnEntity implements IPacket {
	
	private static final AbstractReflectionUtil.SaveConstructor PACKET_OPT_1 = ReflectionUtil.getDeclaredConstructor("PacketPlayOutSpawnEntity",
			ReflectionUtil.getNmsClass("Entity"), int.class, int.class);
	private static final AbstractReflectionUtil.SaveConstructor PACKET_OPT_2 = ReflectionUtil.getDeclaredConstructor("PacketPlayOutSpawnEntity",
			ReflectionUtil.getNmsClass("Entity"), int.class, int.class,
			ReflectionUtil.getNmsClass("BlockPosition"));
	
	private Object entity;
	private int type;
	private int metaData;
	private BlockPos pos;
	
	public PPOSpawnEntity(Object entity, int type, int metaData) {
		this.entity = entity;
		this.type = type;
		this.metaData = metaData;
	}
	
	public PPOSpawnEntity(Object entity, int type, int metaData, BlockPos pos) {
		this.entity = entity;
		this.type = type;
		this.metaData = metaData;
		this.pos = pos;
	}
	
	@Override
	public Object getPacket(boolean stackTrace) {
		if(pos == null)
			return PACKET_OPT_1.newInstance(stackTrace, entity, type, metaData);
		return PACKET_OPT_2.newInstance(stackTrace, entity, type, metaData, ReflectionUtil.toBlockPosition(pos));
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getEntity(), getType(), getMetaData(), getPos());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		PPOSpawnEntity that = (PPOSpawnEntity) o;
		return getType() == that.getType() &&
				getMetaData() == that.getMetaData() &&
				Objects.equal(getEntity(), that.getEntity()) &&
				Objects.equal(getPos(), that.getPos());
	}
	
	@Override
	public String toString() {
		return "PPOSpawnEntity{" +
				"entity=" + entity +
				", type=" + type +
				", metaData=" + metaData +
				", pos=" + pos +
				'}';
	}
	
	public Object getEntity() {
		return entity;
	}
	
	public int getType() {
		return type;
	}
	
	public PPOSpawnEntity setType(int type) {
		this.type = type;
		return this;
	}
	
	public int getMetaData() {
		return metaData;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public PPOSpawnEntity setPos(BlockPos pos) {
		this.pos = pos;
		return this;
	}
	
	public PPOSpawnEntity setMetaData(int metaData) {
		this.metaData = metaData;
		return this;
	}
	
	public PPOSpawnEntity setEntity(Object entity) {
		this.entity = entity;
		return this;
	}
}
